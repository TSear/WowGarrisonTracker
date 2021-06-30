package com.trix.wowgarrisontracker.pojos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountCharacterForm {

    private String accountCharacterName;
    private Long accountId;

    public AccountCharacterForm() {
        this.accountCharacterName = "";
        this.accountId = 0L;
    }
}
