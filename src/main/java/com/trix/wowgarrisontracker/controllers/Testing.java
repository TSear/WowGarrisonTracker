package com.trix.wowgarrisontracker.controllers;

import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

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
import com.trix.wowgarrisontracker.validators.LoginRequestValidator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    private LoginRequestValidator loginRequestValidator;

    public Testing(AccountCharacterService accountCharacterService, AccountService accountService,
            EntryService entryService, JWTutils jwTutils, AccountDTOValidator accountDTOValidator,
            LoginRequestValidator loginRequestValidator) {
        this.accountCharacterService = accountCharacterService;
        this.accountService = accountService;
        this.entryService = entryService;
        this.accountPojoToAccount = new AccountPojoToAccount();
        this.accountToAccountPojo = new AccountToAccountPojo();
        this.accountDTOValidator = accountDTOValidator;
        this.jwTutils = jwTutils;
        this.loginRequestValidator = loginRequestValidator;
    }

    @GetMapping("login/page")
    public String loginPage(Model model) {
        if (!model.containsAttribute("loginRequest")) {
            LoginRequest loginRequest = new LoginRequest();
            loginRequest.setLogin("Login");
            model.addAttribute("loginRequest", loginRequest);
        }
        return "login";
    }

    @PostMapping(value = "login/validate")
    public String login(@ModelAttribute("loginRequest") LoginRequest loginRequest, Model model,
            BindingResult bindingResult, RedirectAttributes redirectAttributes, HttpServletResponse httpServletResponse) {

        boolean isPasswordCorrect = false;
        boolean isLoginInDatabase = accountService.isExisting(loginRequest);
        Account account = accountService.findUserByUsername(loginRequest.getLogin());

        if (account != null)
            isPasswordCorrect = loginRequest.getPassword().equals(account.getPassword());

        if (isLoginInDatabase && isPasswordCorrect) {
            logger.info("Procesing credentials");
            model.addAttribute("token", jwTutils.generateToken(new CustomUserDetails(account)));
            //TODO JWT token jest przechowywany w ciasteczku. Będzie trzeba to zmienić na coś bardziej bezpiecznego
            Cookie cookie = new Cookie("Authorization", jwTutils.generateToken(new CustomUserDetails(account)));
            cookie.setPath("/");
            httpServletResponse.addCookie(cookie);
            return "redirect:/testing/get";
        }

        bindingResult.reject("credentials.bad", "Wrong login or password");
        redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.loginRequest",
                bindingResult);
        redirectAttributes.addFlashAttribute("loginRequest", loginRequest);
        return "redirect:/testing/login/page";

    }

    @ResponseBody
    @PostMapping(value = "login/rest")
    public String restLogin(@RequestBody LoginRequest loginRequest) {
        Account account = accountService.correctCredentials(loginRequest);
        if (account != null) {
            logger.info("Procesing credentials");
            return jwTutils.generateToken(new CustomUserDetails(account));
        }
        return "bad credentials";
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