package com.olive.chatapp.config;

import com.olive.chatapp.model.User;
import com.olive.chatapp.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRep;

    @Autowired
    public CustomUserDetailsService(UserRepository userRep) {
        this.userRep = userRep;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRep.findByUsername(username);
        if (user.isPresent()) {
            return org.springframework.security.core.userdetails.User.builder().username(user.get().getUsername()).build();
        }
        else{
            throw new UsernameNotFoundException(username);
        }
    }
}
