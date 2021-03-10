package com.trix.wowgarrisontracker.validators;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.trix.wowgarrisontracker.pojos.RegisterModel;
import com.trix.wowgarrisontracker.services.interfaces.AccountService;
@Component
public class RegisterModelValidator implements Validator {

	private AccountService accountService;

	public RegisterModelValidator(AccountService accountService) {
		this.accountService = accountService;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return clazz.isInstance(RegisterModel.class);
	}

	@Override
	public void validate(Object target, Errors errors) {
		RegisterModel registerModel = (RegisterModel) target;
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "login", "login.empty", "Login cannot be empty");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "password.empty", "Password cannot be empty");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "repeatedPassword", "repeatedPassword.empty",
				"Repeated Password cannot be empty");
		if (!registerModel.getPassword()
				.equals(registerModel.getRepeatedPassword())) {
			errors.rejectValue("password", "password.dontmatch", "Password are not matching");
		}
		if(accountService.areCredentialsTaken(registerModel)) {
			errors.rejectValue("login", "login.taken", "Login is already taken");
		}
		if(registerModel.isLoginTooLong())
			errors.rejectValue("login", "login.tooLong", "Login is too long!");
		if(registerModel.isPasswordTooLong())
			errors.rejectValue("password", "password.tooLong", "Password is too long!");
		if(registerModel.isRepeatedPasswordTooLong())
			errors.rejectValue("repeatedPassword", "repeatedPassword.tooLong", "Repeated Password is too long!");

	}

}
