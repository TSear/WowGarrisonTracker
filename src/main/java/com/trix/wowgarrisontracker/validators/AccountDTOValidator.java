package com.trix.wowgarrisontracker.validators;

import com.trix.wowgarrisontracker.pojos.AccountPojo;
import com.trix.wowgarrisontracker.repository.AccountRepository;
import com.trix.wowgarrisontracker.services.interfaces.AccountService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class AccountDTOValidator implements Validator {

    @Autowired
    private AccountService AccountService;
    

    @Override
    public boolean supports(Class<?> clazz) {
        return AccountPojo.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        AccountPojo pojo = AccountPojo.class.cast(target);
        ValidationUtils.rejectIfEmpty(errors, "login", "login.empty", "Login nie może być pusty");
        ValidationUtils.rejectIfEmpty(errors, "password", "password.empty", "Hasło nie może być puste");
        if(AccountService.findUserByUsername(pojo.getLogin())!=null)
            errors.rejectValue("login", "login.alreadyexist", "Taki login już istnieje");
    }

 

}
