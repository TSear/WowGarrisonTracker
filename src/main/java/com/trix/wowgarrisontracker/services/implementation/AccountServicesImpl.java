package com.trix.wowgarrisontracker.services.implementation;

import com.trix.wowgarrisontracker.converters.AccountPojoToAccount;
import com.trix.wowgarrisontracker.converters.AccountToAccountPojo;
import com.trix.wowgarrisontracker.exceptions.AccountNotFoundException;
import com.trix.wowgarrisontracker.model.Account;
import com.trix.wowgarrisontracker.model.LoginRequest;
import com.trix.wowgarrisontracker.pojos.AccountPojo;
import com.trix.wowgarrisontracker.pojos.RegisterModel;
import com.trix.wowgarrisontracker.pojos.RegisterPojo;
import com.trix.wowgarrisontracker.repository.AccountRepository;
import com.trix.wowgarrisontracker.services.interfaces.AccountService;
import com.trix.wowgarrisontracker.services.interfaces.OptionsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service

public class AccountServicesImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final AccountToAccountPojo accountToAccountPojo;
    private final PasswordEncoder passwordEncoder;
    private final AccountPojoToAccount accountPojoToAccount;
    private final OptionsService optionsService;

    public AccountServicesImpl(AccountRepository accountRepository, AccountToAccountPojo accountToAccountPojo, PasswordEncoder passwordEncoder, AccountPojoToAccount accountPojoToAccount, OptionsService optionsService) {
        this.accountRepository = accountRepository;
        this.accountToAccountPojo = accountToAccountPojo;
        this.passwordEncoder = passwordEncoder;
        this.accountPojoToAccount = accountPojoToAccount;
        this.optionsService = optionsService;
    }

    @Override
    public void saveAccount(Account account) {

        if (!account.getLogin()
                .isEmpty() && this.findUserByUsername(account.getLogin()) == null) {
            account.setPassword(passwordEncoder.encode(account.getPassword()));
            accountRepository.save(account);
        }

    }

    @Override
    public void createAccount(RegisterPojo registerPojo) {


        Account account = new Account();
        account.getOptions().setServer(registerPojo.getServer());

        account.setPassword(passwordEncoder.encode(registerPojo.getPassword()));
        account.setLogin(registerPojo.getLogin());

        accountRepository.save(account);

    }

    @Override
    public void deleteAccount(Long id) {

        if (accountRepository.existsById(id))
            accountRepository.deleteById(id);

    }

    @Override
    public List<AccountPojo> findAllAccounts() {
        List<AccountPojo> accountPojos = new ArrayList<>();

        for (Account tmp : accountRepository.findAll()) {
            accountPojos.add(accountToAccountPojo.convert(tmp));
        }
        return accountPojos;
    }

    @Override
    public Account findUserByUsername(String username) {
        Optional<Account> optionalAccount = accountRepository.findByLogin(username);
        return optionalAccount.orElse(null);

    }

    @Override
    public Account correctCredentials(LoginRequest fromForm) {

        Account account = this.findUserByUsername(fromForm.getLogin());

        if (account != null && account.getPassword()
                .equals(fromForm.getPassword())) {
            return account;
        }
        return null;
    }

    @Override
    public Account areCredentialsCorrect(String login, String password) throws AccountNotFoundException {

        Account account = this.findUserByUsername(login);

        if (account != null && passwordEncoder.matches(password, account.getPassword())) {
            return account;
        }

        throw new AccountNotFoundException();
    }

    @Override
    public boolean isAccountInDatabase(LoginRequest loginRequest) {
        Account account = this.findUserByUsername(loginRequest.getLogin());
        if (account == null)
            return false;
        return passwordEncoder.matches(loginRequest.getPassword(), account.getPassword());
    }


    @Override
    public boolean areCredentialsTaken(RegisterModel registerModel) {
        Account account = this.findUserByUsername(registerModel.getLogin());
        return account != null;
    }


    @Override
    public Account findById(Long id) {
        Optional<Account> optionalAccount = accountRepository.findById(id);
        return optionalAccount.orElse(null);
    }


}

