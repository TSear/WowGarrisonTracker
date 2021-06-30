package com.trix.wowgarrisontracker.pojos;

import com.trix.wowgarrisontracker.model.Account;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
public class Credentials {

    private String oldPassword;

    private String newPassword;

    @Email
    private String email;

    public Credentials(Account account) {
        this.oldPassword = account.getPassword();
        this.email = account.getEmail();
    }
}
