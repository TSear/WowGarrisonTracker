package com.trix.wowgarrisontracker.pojos;

import lombok.Data;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Data
public class AccountPojo {

    private Long id;
    private String login;
    private String password;
    private Set<AccountCharacterPojo> accountCharacters;
    private Long garrisonResources;
    private Long warPaint;
    private Long amountOfEntries;
    private Long days;
    private double averageGRPerDay;
    private double averageWPPerDay;

    public AccountPojo() {
        id = 1l;
        login = "";
        password = "";
        accountCharacters = new HashSet<>(Collections.emptySet());
        garrisonResources = 0l;
        warPaint = 0l;
        amountOfEntries = 0l;
    }

}
