package com.olive.chatapp.service;

import com.olive.chatapp.model.LoginAuth;
import com.olive.chatapp.model.User;
import com.olive.chatapp.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.Optional;

@Service
public class UserPersistenceServices {

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private final UserRepository userRepository;

    @Autowired
    public UserPersistenceServices(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(User newUser){
        String hashedPassword = passwordEncoder.encode(newUser.getPassword());
        newUser.setPassword(hashedPassword);
        return userRepository.save(newUser);
    }

    public Optional<User> findUserByName(String userName){
        return userRepository.findByUsername(userName);
    }

    public User passwordCheck(LoginAuth auth) throws InvalidParameterException {
        Optional<User> user=findUserByName(auth.getUsername());
        if(user.isEmpty()){
            throw new InvalidParameterException("Invalid username");
        }
        if(!passwordEncoder.matches(auth.getPassword(),user.get().getPassword())){
            throw new InvalidParameterException("Invalid password");
        }
        return user.get();
    }
}
