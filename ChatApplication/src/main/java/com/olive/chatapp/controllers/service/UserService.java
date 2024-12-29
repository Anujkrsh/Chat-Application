package com.olive.chatapp.controllers.service;

import com.olive.chatapp.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public interface UserService {

    public ResponseEntity<?> createUser(User user);
}
