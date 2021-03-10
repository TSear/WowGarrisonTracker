package com.trix.wowgarrisontracker.validators;

import com.trix.wowgarrisontracker.model.LoginRequest;
import com.trix.wowgarrisontracker.services.interfaces.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class LoginRequestValidator implements Validator {

    @Autowired
    private AccountService accountService;


    @Override
    public boolean supports(Class<?> clazz) {
        return LoginRequest.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        LoginRequest loginRequest = LoginRequest.class.cast(target);
        ValidationUtils.rejectIfEmpty(errors, "login", "login.empty", "Login can't be empty");
        ValidationUtils.rejectIfEmpty(errors, "password", "password.empty", "Password can't be empty");

        if (!accountService.isAccountInDatabase(loginRequest)) {
            errors.reject("account.notexist", "Wrong login or password");
        }
        if (loginRequest.isLoginTooLong())
            errors.rejectValue("login", "login.tooLong", "Login Too long");
        if (loginRequest.isPasswordTooLong())
            errors.rejectValue("password", "password.tooLong", "Password Too long");

    }




}
