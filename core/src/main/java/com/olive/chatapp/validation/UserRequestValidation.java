package com.olive.chatapp.validation;

import com.olive.chatapp.model.User;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserRequestValidation {

    ValidatorFactory factory= Validation.buildDefaultValidatorFactory();
    Validator validator = factory.getValidator();

    public String checkRegistrationRequest(User user) {
        Set<ConstraintViolation<User>> violations= validator.validate(user);
        if(!violations.isEmpty()) {
            return violations.iterator().next().getMessage();
        }
        return null;
    }
}
