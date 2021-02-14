package com.trix.wowgarrisontracker.pojos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterModel {
	private String login;
	private String password;
	private String repeatedPassword;
}
