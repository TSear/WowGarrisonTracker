package com.trix.wowgarrisontracker.services.implementation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.trix.wowgarrisontracker.converters.AccountToAccountPojo;
import com.trix.wowgarrisontracker.model.Account;
import com.trix.wowgarrisontracker.model.AccountCharacter;
import com.trix.wowgarrisontracker.model.Entry;
import com.trix.wowgarrisontracker.model.LoginRequest;
import com.trix.wowgarrisontracker.pojos.AccountPojo;
import com.trix.wowgarrisontracker.repository.AccountRepository;
import com.trix.wowgarrisontracker.services.interfaces.AccountCharacterService;
import com.trix.wowgarrisontracker.services.interfaces.AccountService;
import com.trix.wowgarrisontracker.services.interfaces.EntryService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
public class AccountServicesImpl implements AccountService {

    private AccountRepository accountRepository;
    private Logger logger = LoggerFactory.getLogger(Slf4j.class);
    private AccountToAccountPojo accountToAccountPojo;
    private AccountCharacterService accountCharacterService;
    private EntryService entryService;

    public AccountServicesImpl(AccountRepository accountRepository, AccountToAccountPojo accountToAccountPojo, EntryService entryService,
    AccountCharacterService accountCharacterService) {
        this.accountRepository = accountRepository;
        this.accountToAccountPojo = accountToAccountPojo;
        this.accountCharacterService = accountCharacterService;
        this.entryService = entryService;
    }

    @Override
    public void save(Account account) {
        logger.info(account.toString());

        // TODO Tymczasowe zabezpieczenie
        if (!account.getLogin().isEmpty() && this.findUserByUsername(account.getLogin()) == null)
            accountRepository.save(account);

    }

    @Override
    public void delete(Long id) {

        if (accountRepository.existsById(id))
            accountRepository.deleteById(id);
        else
            logger.info("Nie znaleziono konta");

    }

    @Override
    public void update(Account account, Long id) {

        if (accountRepository.existsById(id)) {
            account.setId(id);
            accountRepository.save(account);
        } else {
            logger.info("Nie znaleziono konta");
        }

    }

    @Override
    public List<AccountPojo> findAll() {
        List<AccountPojo> accountPojos = new ArrayList<>();

        for (Account tmp : accountRepository.findAll()) {
            accountPojos.add(accountToAccountPojo.convert(tmp));
        }
        return accountPojos;
    }

    @Override
    public Account findUserByUsername(String username) {
        Optional<Account> optionalAccount = accountRepository.findByLogin(username);
        return optionalAccount.isPresent() ? optionalAccount.get() : null;

    }

    @Override
    public boolean correctCredentials(Account inDatabase, LoginRequest fromForm) {

        return inDatabase.getLogin().equals(fromForm.getLogin())
                && inDatabase.getPassword().equals(fromForm.getPassword());

    }

    @Override
    public Account correctCredentials(LoginRequest fromForm) {

        Account account = this.findUserByUsername(fromForm.getLogin());
        if (account != null && account.getPassword().equals(fromForm.getPassword())) {
            return account;
        }
        return null;
    }

    @Override
    public boolean isExisting(LoginRequest loginRequest) {
        Account account = this.findUserByUsername(loginRequest.getLogin());
        if (account == null)
            return false;
        return account.getPassword().equals(loginRequest.getPassword());
    }

    @Override
    public List<Entry> getAllEntries(Long accountId) {
        
        List<AccountCharacter> listOfAccountCharacters = accountCharacterService.listOfAccountCharacters(accountId);
        List<Entry> listOfAllEntries = new ArrayList<>();
        for(AccountCharacter tmp : listOfAccountCharacters){
            listOfAllEntries.addAll(entryService.listOfEntries(tmp.getId()));
        }
        
        return listOfAllEntries;
    }

    

}
