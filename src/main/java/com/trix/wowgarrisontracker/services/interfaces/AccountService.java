package com.trix.wowgarrisontracker.services.interfaces;

import com.trix.wowgarrisontracker.exceptions.AccountNotFoundException;
import com.trix.wowgarrisontracker.model.Account;
import com.trix.wowgarrisontracker.pojos.RegisterPojo;

public interface AccountService {


    boolean createAccount(RegisterPojo registerPojo);

    boolean isLoginTaken(String login);

    Account findAccountByLogin(String username);

    Account attemptToLogIn(String login, String password) throws AccountNotFoundException;


    Account findById(Long id);


}
