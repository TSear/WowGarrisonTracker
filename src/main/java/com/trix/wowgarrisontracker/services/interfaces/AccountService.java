package com.trix.wowgarrisontracker.services.interfaces;

import com.trix.wowgarrisontracker.exceptions.AccountNotFoundException;
import com.trix.wowgarrisontracker.model.Account;
import com.trix.wowgarrisontracker.pojos.AccountPojo;
import com.trix.wowgarrisontracker.pojos.RegisterPojo;
import org.springframework.security.core.userdetails.User;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

public interface AccountService {


    boolean isLoginTaken(String login);

    Account findAccountByLogin(String username);

    Account attemptToLogIn(String login, String password) throws AccountNotFoundException;

    Account findById(Long id);

    boolean register(RegisterPojo user, String siteUrl);

    void verificationEmail(Account user, String siteUrl) throws MessagingException, UnsupportedEncodingException;

    boolean verify(String verificationCode);


}
