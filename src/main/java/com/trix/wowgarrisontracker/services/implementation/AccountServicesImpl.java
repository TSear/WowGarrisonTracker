package com.trix.wowgarrisontracker.services.implementation;

import com.trix.wowgarrisontracker.model.Account;
import com.trix.wowgarrisontracker.pojos.RegisterPojo;
import com.trix.wowgarrisontracker.repository.AccountRepository;
import com.trix.wowgarrisontracker.services.interfaces.AccountService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service

public class AccountServicesImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public AccountServicesImpl(AccountRepository accountRepository,
                               PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public boolean createAccount(RegisterPojo registerPojo) {

        if (registerPojo != null && !isLoginTaken(registerPojo.getLogin())) {

            Account account = createAccountFromRegisterPojo(registerPojo);
            accountRepository.save(account);

            return true;

        }

        return false;
    }

    private Account createAccountFromRegisterPojo(RegisterPojo registerPojo) {

        Account account = new Account();
        account.getOptions().setServer(registerPojo.getServer());
        account.setPassword(passwordEncoder.encode(registerPojo.getPassword()));
        account.setLogin(registerPojo.getLogin());

        return account;
    }

    @Override
    public boolean isLoginTaken(String login) {
        if (login == null) {
            return false;
        }
        return accountRepository.existsByLogin(login);
    }

    @Override
    public Account findAccountByLogin(String login) {
        Optional<Account> optionalAccount = accountRepository.findByLogin(login);
        return optionalAccount.orElse(null);

    }

    @Override
    public Account attemptToLogIn(String login, String password){

        Account account = this.findAccountByLogin(login);

        if (account != null && passwordEncoder.matches(password, account.getPassword())) {
            return account;
        }

        return null;
    }


    @Override
    public Account findById(Long id) {
        Optional<Account> optionalAccount = accountRepository.findById(id);
        return optionalAccount.orElse(null);
    }


}

