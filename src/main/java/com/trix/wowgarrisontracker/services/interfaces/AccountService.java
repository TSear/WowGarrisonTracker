package com.trix.wowgarrisontracker.services.interfaces;

import com.trix.wowgarrisontracker.exceptions.AccountNotFoundException;
import com.trix.wowgarrisontracker.model.Account;
import com.trix.wowgarrisontracker.model.CardsOfOmen;
import com.trix.wowgarrisontracker.pojos.RegisterPojo;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

public interface AccountService {

    boolean save(Account account);

    boolean isLoginTaken(String login);

    Account findAccountByLogin(String username);

    Account attemptToLogIn(String login, String password) throws AccountNotFoundException;

    Account findById(Long id);

    boolean register(RegisterPojo user, String siteUrl);

    void verificationEmail(Account user, String siteUrl) throws MessagingException, UnsupportedEncodingException;

    boolean verify(String verificationCode);


    boolean addCards(CardsOfOmen cards);

    boolean removeCards(CardsOfOmen cards);

    Long sumEntriesGarrisonResources(Long accountId);

    Long sumEntriesWarPaint(Long accountId);

    Long countEntries(Long accountId);

    Long countDays(Long accountId);


    boolean isEmailTaken(String s);
}
