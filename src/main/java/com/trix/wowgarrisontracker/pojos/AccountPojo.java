package com.trix.wowgarrisontracker.pojos;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Data
public class AccountPojo {

    private Long id;
    private String login;
    private String password;
    private Set<AccountCharacterPojo> accountCharacters;
    
    public AccountPojo(){
        id = 1l;
        login = "";
        password = "";
        accountCharacters = new HashSet<>(Collections.emptySet());
    }

}
