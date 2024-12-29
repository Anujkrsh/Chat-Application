package com.olive.chatapp.controllers.Implementations;

import com.olive.chatapp.model.User;
import com.olive.chatapp.controllers.service.UserService;
import com.olive.chatapp.service.UserPersistenceServices;
import com.olive.chatapp.validation.UserRequestValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserService {

    private final UserPersistenceServices persistence;

    private final UserRequestValidation requestValidation;

    @Autowired
    public UserServiceImpl(UserRequestValidation requestValidation, UserPersistenceServices persistence) {
        this.requestValidation = requestValidation;
        this.persistence = persistence;
    }

    @Override
    public ResponseEntity<?> createUser(User user) {
        String reqValidation=requestValidation.checkRegistrationRequest(user);
        if( reqValidation!= null) return new ResponseEntity<>(reqValidation, HttpStatusCode.valueOf(400));
        User savedUser =persistence.createUser(user);
        return new ResponseEntity<>(savedUser.getUsername()+ "Created",HttpStatusCode.valueOf(200));
    }
}
