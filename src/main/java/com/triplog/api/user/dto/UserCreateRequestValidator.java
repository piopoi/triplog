package com.triplog.api.user.dto;

import static com.triplog.api.user.constants.UserConstants.MESSAGE_USER_PASSWORD_LENGTH_MIN;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserCreateRequestValidator implements Validator {

    @Value("${user.password.length.min}")
    private int passwordLengthMin;

    @Override
    public boolean supports(Class<?> clazz) {
        return UserCreateRequest.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserCreateRequest userCreateRequest = (UserCreateRequest) target;

        if(userCreateRequest.getPassword().length() < passwordLengthMin) {
            errors.rejectValue("password", "length_min", MESSAGE_USER_PASSWORD_LENGTH_MIN);
        }
    }
}
