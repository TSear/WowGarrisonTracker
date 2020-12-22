package com.trix.wowgarrisontracker.services.implementation;

import java.util.ArrayList;
import java.util.List;

import com.trix.wowgarrisontracker.converters.AccountToAccountPojo;
import com.trix.wowgarrisontracker.model.Account;
import com.trix.wowgarrisontracker.pojos.AccountPojo;
import com.trix.wowgarrisontracker.repository.AccountRepository;
import com.trix.wowgarrisontracker.services.interfaces.AccountService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
public class AccountServicesImpl implements AccountService {

    private AccountRepository accountRepository;
    private Logger logger = LoggerFactory.getLogger(Slf4j.class);
    private AccountToAccountPojo accountToAccountPojo;

    public AccountServicesImpl(AccountRepository accountRepository, AccountToAccountPojo accountToAccountPojo) {
        this.accountRepository = accountRepository;
        this.accountToAccountPojo = accountToAccountPojo;
    }

    @Override
    public void save(Account account) {
        logger.info(account.toString());
        
        //Tymczasowe zabezpieczenie
        if(account.getLogin().length()>1)
            accountRepository.save(account);

    }

    @Override
    public void delete(Long id) {

        if(accountRepository.existsById(id))
            accountRepository.deleteById(id);
        else
            logger.info("Nie znaleziono konta");

    }

    @Override
    public void update(Account account, Long id) {
        
        if(accountRepository.existsById(id)){
            account.setId(id);
            accountRepository.save(account);
        }else{
            logger.info("Nie znaleziono konta");
        }

    }

    @Override
    public List<AccountPojo> findAll() {
        List<AccountPojo> accountPojos = new ArrayList<>();

        for(Account tmp : accountRepository.findAll()){
            accountPojos.add(accountToAccountPojo.convert(tmp));
        }
        return accountPojos;
    }
    
}
