package com.trix.wowgarrisontracker.controllers;

import java.util.List;

import com.trix.wowgarrisontracker.converters.AccountPojoToAccount;
import com.trix.wowgarrisontracker.converters.AccountToAccountPojo;
import com.trix.wowgarrisontracker.pojos.AccountPojo;
import com.trix.wowgarrisontracker.services.interfaces.AccountCharacterService;
import com.trix.wowgarrisontracker.services.interfaces.AccountService;
import com.trix.wowgarrisontracker.services.interfaces.EntryService;
import com.trix.wowgarrisontracker.validators.AccountDTOValidator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RequestMapping("testing/")
@RestController
public class Testing {

    private AccountCharacterService accountCharacterService;
    private AccountService accountService;
    private EntryService entryService;
    private Logger logger = LoggerFactory.getLogger(Slf4j.class);
    private AccountPojoToAccount accountPojoToAccount;
    private AccountToAccountPojo accountToAccountPojo;
    private AccountDTOValidator accountDTOValidator;

    public Testing(AccountCharacterService accountCharacterService, AccountService accountService,
            EntryService entryService) {
        this.accountCharacterService = accountCharacterService;
        this.accountService = accountService;
        this.entryService = entryService;
        this.accountPojoToAccount = new AccountPojoToAccount();
        this.accountToAccountPojo = new AccountToAccountPojo();
        this.accountDTOValidator = new AccountDTOValidator();
    }

    @RequestMapping("get")
    public List<AccountPojo> getObjects() {

        return accountService.findAll();
    }

    @RequestMapping("create")
    public String saveAccount(@RequestBody AccountPojo account, BindingResult bindingResult) {
        accountDTOValidator.validate(account, bindingResult);
        if (!bindingResult.hasErrors()) {
            accountService.save(accountPojoToAccount.convert(account));
            return "saving";
        }
        String errorMessage = "";
        StringBuilder errorMessageBuilder = new StringBuilder(errorMessage);
        for(FieldError tmp : bindingResult.getFieldErrors()){
            errorMessageBuilder.append(tmp.getDefaultMessage());
            errorMessageBuilder.append("\n");
        }
        errorMessage = errorMessageBuilder.toString();
        return errorMessage;

    }

    @RequestMapping("delete")
    public String deleteAccount(@RequestBody Long id) {

        accountService.delete(id);

        return "asdfasdfasdfasdf";
    }

    @RequestMapping("update")
    public String updateAccount(@Validated @RequestBody AccountPojo account, BindingResult bindingResult, Long id) {

        accountService.update(accountPojoToAccount.convert(account), id);

        return "asdfasdfasdfasdf";
    }

}