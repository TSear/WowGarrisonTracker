package com.trix.wowgarrisontracker.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    
    private String login;
    private String password;

    public boolean isLoginTooLong() {
        return login.length() > 50;
    }
    public boolean isPasswordTooLong() {
        return password.length() > 50;
    }
}
