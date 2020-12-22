package com.trix.wowgarrisontracker.controllers;

import java.util.List;

import com.trix.wowgarrisontracker.model.Account;
import com.trix.wowgarrisontracker.repository.AccountRepository;
import com.trix.wowgarrisontracker.services.interfaces.AccountCharacterService;
import com.trix.wowgarrisontracker.services.interfaces.AccountService;
import com.trix.wowgarrisontracker.services.interfaces.EntryService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RequestMapping("testing/")
@RestController
public class Testing{

    private AccountCharacterService accountCharacterService;
    private AccountService accountService;
    private EntryService entryService;
    private Logger logger = LoggerFactory.getLogger(Slf4j.class);


    public Testing(AccountCharacterService accountCharacterService, AccountService accountService, EntryService entryService){
        this.accountCharacterService = accountCharacterService;
        this.accountService = accountService;
        this.entryService = entryService;
    }

    @RequestMapping("get")
    public List<Account> getObjects(){

        return accountService.findAll();
    }

    @RequestMapping("create")
    public String saveAccount(Account account){

        accountService.save(account);

        return "asdfasdfasdfasdf";
    }

    @RequestMapping("delete")
    public String deleteAccount(Long id){

        accountService.delete(id);

        return "asdfasdfasdfasdf";
    }

    @RequestMapping("update")
    public String updateAccount(Account account, Long id){

        accountService.update(account, id);

        return "asdfasdfasdfasdf";
    }


}