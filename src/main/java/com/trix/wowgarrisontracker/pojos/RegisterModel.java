package com.trix.wowgarrisontracker.pojos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterModel {
    private String login;
    private String password;
    private String repeatedPassword;

    public boolean isLoginTooLong() {
        return this.login.length() > 50;
    }

    public boolean isPasswordTooLong() {
        return this.password.length() > 50;
    }

    public boolean isRepeatedPasswordTooLong() {
        return this.repeatedPassword.length() > 50;
    }
}
