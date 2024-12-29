package com.olive.chatapp.service;

import com.olive.chatapp.model.User;
import com.olive.chatapp.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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
}
