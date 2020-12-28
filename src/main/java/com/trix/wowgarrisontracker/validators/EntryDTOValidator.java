package com.trix.wowgarrisontracker.validators;

import com.trix.wowgarrisontracker.pojos.EntryPojo;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class EntryDTOValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return EntryPojo.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        EntryPojo pojo = (EntryPojo) target;
        // TODO Validacja do EntryPojo na razie nie jest potrzebna

    }

}
