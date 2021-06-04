package com.trix.wowgarrisontracker.exceptions;

public class AccountNotFoundException extends Exception {

    public AccountNotFoundException() {
        super("Account was not found in database");
    }

}
