package com.trix.wowgarrisontracker.controllers;

import java.util.List;

import com.trix.wowgarrisontracker.converters.AccountPojoToAccount;
import com.trix.wowgarrisontracker.converters.AccountToAccountPojo;
import com.trix.wowgarrisontracker.model.Account;
import com.trix.wowgarrisontracker.model.CustomUserDetails;
import com.trix.wowgarrisontracker.model.LoginRequest;
import com.trix.wowgarrisontracker.pojos.AccountPojo;
import com.trix.wowgarrisontracker.services.interfaces.AccountCharacterService;
import com.trix.wowgarrisontracker.services.interfaces.AccountService;
import com.trix.wowgarrisontracker.services.interfaces.EntryService;
import com.trix.wowgarrisontracker.utils.JWTutils;
import com.trix.wowgarrisontracker.validators.AccountDTOValidator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.slf4j.Slf4j;

@RequestMapping("testing/")
@Controller
public class Testing {

    private AccountCharacterService accountCharacterService;
    private AccountService accountService;
    private EntryService entryService;
    private Logger logger = LoggerFactory.getLogger(Slf4j.class);
    private AccountPojoToAccount accountPojoToAccount;
    private AccountToAccountPojo accountToAccountPojo;
    private AccountDTOValidator accountDTOValidator;
    private JWTutils jwTutils;

    public Testing(AccountCharacterService accountCharacterService, AccountService accountService,
            EntryService entryService, JWTutils jwTutils, AccountDTOValidator accountDTOValidator) {
        this.accountCharacterService = accountCharacterService;
        this.accountService = accountService;
        this.entryService = entryService;
        this.accountPojoToAccount = new AccountPojoToAccount();
        this.accountToAccountPojo = new AccountToAccountPojo();
        this.accountDTOValidator = accountDTOValidator;
        this.jwTutils = jwTutils;
    }

    @RequestMapping("loginPage")
    public String loginPage(Model model) {
        model.addAttribute("loginRequest", new LoginRequest());
        return "login";
    }

    @PostMapping(value = "login", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String login(@ModelAttribute LoginRequest loginRequest, Model model) {

        Account account = accountService.correctCredentials(loginRequest);

        if (account != null) {
            logger.info("Procesing credentials");
            model.addAttribute("token", jwTutils.generateToken(new CustomUserDetails(account)));
            return "redirect:/testing/get";
        }

        return "redirect:/testing/loginPage";

    }

    @ResponseBody
    @RequestMapping("get")
    public List<AccountPojo> getObjects() {
        return accountService.findAll();
    }

    @ResponseBody
    @PostMapping("create")
    public String saveAccount(@RequestBody AccountPojo account, BindingResult bindingResult) {
        logger.info("creating");
        accountDTOValidator.validate(account, bindingResult);
        if (!bindingResult.hasErrors()) {
            accountService.save(accountPojoToAccount.convert(account));
            return "saving";
        }
        String errorMessage = "";
        StringBuilder errorMessageBuilder = new StringBuilder(errorMessage);
        for (FieldError tmp : bindingResult.getFieldErrors()) {
            errorMessageBuilder.append(tmp.getDefaultMessage());
            errorMessageBuilder.append("\n");
        }
        errorMessage = errorMessageBuilder.toString();
        return errorMessage;

    }

    @ResponseBody
    @RequestMapping("delete")
    public String deleteAccount(@RequestBody Long id) {

        accountService.delete(id);

        return "asdfasdfasdfasdf";
    }

    @ResponseBody
    @RequestMapping("update")
    public String updateAccount(@Validated @RequestBody AccountPojo account, BindingResult bindingResult, Long id) {

        accountService.update(accountPojoToAccount.convert(account), id);

        return "asdfasdfasdfasdf";
    }

}