package com.trix.wowgarrisontracker.validators;

import com.trix.wowgarrisontracker.pojos.AccountCharacterPojo;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class AccountCharacterDTOValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return AccountCharacterPojo.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        AccountCharacterPojo pojo = AccountCharacterPojo.class.cast(target);
        ValidationUtils.rejectIfEmpty(errors,pojo.getCharacterName(), "characterName.empty");
    }
    
}
