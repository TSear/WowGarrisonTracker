package com.trix.wowgarrisontracker.validators;

import com.trix.wowgarrisontracker.pojos.AccountPojo;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class AccountDTOValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return AccountPojo.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        AccountPojo pojo = AccountPojo.class.cast(target);
        ValidationUtils.rejectIfEmpty(errors, "login", "login.empty", "Login nie może być pusty");
        ValidationUtils.rejectIfEmpty(errors, "password", "password.empty", "Hasło nie może być puste");
    }
    
}
