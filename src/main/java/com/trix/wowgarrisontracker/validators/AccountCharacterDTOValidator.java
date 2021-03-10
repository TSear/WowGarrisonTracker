package com.trix.wowgarrisontracker.validators;

import com.trix.wowgarrisontracker.pojos.AccountCharacterPojo;
import com.trix.wowgarrisontracker.services.interfaces.AccountCharacterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class AccountCharacterDTOValidator implements Validator {

    @Autowired
    private AccountCharacterService accountCharacterService;

    @Override
    public boolean supports(Class<?> clazz) {
        return AccountCharacterPojo.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        AccountCharacterPojo pojo = AccountCharacterPojo.class.cast(target);
        ValidationUtils.rejectIfEmpty(errors, "characterName", "characterName.empty", "Character name cannot be empty");
        if (accountCharacterService.isNameTaken(pojo.getAccountId(), pojo.getCharacterName())) {
            errors.rejectValue("characterName", "name.taken", "This name already exist");
        }
        if(pojo.getCharacterName().length()>100){
            errors.rejectValue("characterName","name.toolong", "This name it too long");
        }
    }

}
