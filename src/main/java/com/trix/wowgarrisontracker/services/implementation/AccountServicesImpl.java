package com.trix.wowgarrisontracker.services.implementation;

import com.trix.wowgarrisontracker.repository.AccountRepository;
import com.trix.wowgarrisontracker.services.interfaces.AccountService;

import org.springframework.stereotype.Service;

@Service
public class AccountServicesImpl implements AccountService{

    private AccountRepository accountRepository;

    public AccountServicesImpl(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }
    
}
