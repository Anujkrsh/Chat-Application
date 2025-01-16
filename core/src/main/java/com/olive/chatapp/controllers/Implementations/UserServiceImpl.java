package com.olive.chatapp.controllers.Implementations;

import com.olive.chatapp.model.LoginAuth;
import com.olive.chatapp.model.User;
import com.olive.chatapp.controllers.service.UserService;
import com.olive.chatapp.service.UserPersistenceServices;
import com.olive.chatapp.util.JwtUtil;
import com.olive.chatapp.validation.UserRequestValidation;
import org.slf4j.Logger;

import java.nio.charset.StandardCharsets;
import java.security.InvalidParameterException;
import java.util.Base64;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserPersistenceServices persistence;

    private final UserRequestValidation requestValidation;

    private final JwtUtil jwtUtil;

    @Autowired
    public UserServiceImpl(UserRequestValidation requestValidation, UserPersistenceServices persistence,JwtUtil jwtUtil) {
        this.requestValidation = requestValidation;
        this.persistence = persistence;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public ResponseEntity<?> createUser(User user) {
        String reqValidation=requestValidation.checkRegistrationRequest(user);
        if( reqValidation!= null) return new ResponseEntity<>(reqValidation, HttpStatusCode.valueOf(400));
        Optional<User> user1=persistence.findUserByName(user.getUsername());
        if(user1.isPresent()) {
            log.info("User with Username:{} already Present!",user.getUsername());
            return new ResponseEntity<>("User with Username: "+user.getUsername()+" already Present!",HttpStatusCode.valueOf(403));
        }
        User savedUser =persistence.createUser(user);
        log.info("User saved: {}",user.getUsername());
        return new ResponseEntity<>(savedUser.getUsername()+ " Created",HttpStatusCode.valueOf(200));
    }

    @Override
    public ResponseEntity<?> userLogin(String auth) {
        String jwt;
        LoginAuth credentials = extractCredentials(auth);
        if(credentials == null) {
           return new ResponseEntity<>("Invalid User Credentials",HttpStatusCode.valueOf(400));
        }
        try{
            User userForJwt=persistence.passwordCheck(credentials);
            jwt=jwtUtil.generateToken(userForJwt.getUsername());
        }catch (InvalidParameterException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatusCode.valueOf(400));
        }
        return new ResponseEntity<>("jwt: "+jwt,HttpStatusCode.valueOf(200));
    }

    public LoginAuth extractCredentials(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Basic ")) {
            String base64Credentials = authorizationHeader.substring("Basic ".length()).trim();
            try {
                byte[] decodedBytes = Base64.getDecoder().decode(base64Credentials);
                String credentials = new String(decodedBytes, StandardCharsets.UTF_8);
                String[] parts = credentials.split(":", 2);
                if (parts.length == 2) {
                    return LoginAuth.builder().username(parts[0]).password(parts[1]).build();
                } else {
                   log.error("Invalid Basic Auth credentials format");
                    return null;
                }
            } catch (IllegalArgumentException e) {
                log.error("Invalid Base64 encoding: {}", e.getMessage());
                return null;
            }
        } else {
            log.error("Authorization header is missing or not Basic Auth");
            return null;
        }
    }

}
