package com.trix.wowgarrisontracker.frontEnd;

import com.trix.wowgarrisontracker.enums.SecurityValues;
import com.trix.wowgarrisontracker.exceptions.AccountNotFoundException;
import com.trix.wowgarrisontracker.model.Account;
import com.trix.wowgarrisontracker.model.LoginRequest;
import com.trix.wowgarrisontracker.services.interfaces.AccountService;
import com.trix.wowgarrisontracker.utils.GeneralUtils;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.page.Page;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import java.util.ArrayList;

@Component
@UIScope
@Profile("vaadin")
@Route(value = "login")
public class LoginPage extends FlexLayout {

    private AccountService accountService;
    private Binder<LoginRequest> loginRequestBinder;
    private LoginRequest loginRequest;
    private GeneralUtils utils;

    public LoginPage(AccountService accountService, GeneralUtils utils) {

        this.accountService = accountService;
        this.utils = utils;

        this.loginRequestBinder = new Binder<>();
        this.loginRequest = new LoginRequest();
    }



    @PostConstruct
    private void initialize() {

        LoginForm loginForm = new LoginForm();
        loginForm.setAction("login");
//        loginForm.addLoginListener(loginEvent -> {
//            String login = loginEvent.getUsername();
//            String password = loginEvent.getPassword();
//            try {
//                Account account = accountService.areCredentialsCorrect(login, password);
//                Cookie jwtCookie = utils.createJWTCookie(account.getLogin());
//                VaadinService.getCurrentResponse().addCookie(jwtCookie);
//                SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(loginEvent.getUsername(), loginEvent.getPassword(), new ArrayList<>()));
//                UI.getCurrent().navigate(TrackLayout.class);
//
//
//            } catch (AccountNotFoundException e) {
//                loginForm.setError(true);
//                System.out.println(e.getMessage());
//            }
//        });

        add(loginForm);
    }



}
