package com.trix.wowgarrisontracker.validators;

import com.trix.wowgarrisontracker.pojos.EntryPojo;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class EntryDTOValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return EntryPojo.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        EntryPojo pojo = (EntryPojo) target;
        ValidationUtils.rejectIfEmpty(errors, "garrisonResources", "garrisonResources.empty", "Garrison Resources cannot be empty");
        ValidationUtils.rejectIfEmpty(errors, "warPaint", "warPaint.empty", "War paint cannot be empty");
        ValidationUtils.rejectIfEmpty(errors, "accountCharacterId", "accountCharacterId.empty", "Account Character cannot be empty");
        if(pojo.getGarrisonResources() < 0)errors.rejectValue("garrisonResources", "garrisonResources.negative", "Garrison Resources cannot be negative");
        if(pojo.getWarPaint() < 0)errors.rejectValue("warPaint", "warPaint.negative", "War Paint cannot be negative");
    }

}
