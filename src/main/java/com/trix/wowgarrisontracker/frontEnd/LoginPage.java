package com.trix.wowgarrisontracker.frontEnd;

import com.trix.wowgarrisontracker.exceptions.AccountNotFoundException;
import com.trix.wowgarrisontracker.model.Account;
import com.trix.wowgarrisontracker.model.LoginRequest;
import com.trix.wowgarrisontracker.services.interfaces.AccountService;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@UIScope
@Profile("vaadin")
@Route(value = "login")
public class LoginPage extends FlexLayout {

    private AccountService accountService;
    private Binder<LoginRequest> loginRequestBinder;
    private LoginRequest loginRequest;

    public LoginPage(AccountService accountService) {

        this.accountService = accountService;

        this.loginRequestBinder = new Binder<>();
        this.loginRequest = new LoginRequest();
    }



    @PostConstruct
    private void initialize() {

        LoginForm loginForm = new LoginForm();
        loginForm.setAction("login");
        loginForm.addLoginListener(loginEvent -> {
            String login = loginEvent.getUsername();
            String password = loginEvent.getPassword();
            try {
                Account account = accountService.areCredentialsCorrect(login, password);
                VaadinSession.getCurrent().setAttribute("id", account.getId());
            } catch (AccountNotFoundException e) {
                loginForm.setError(true);
                System.out.println(e.getMessage());
            }
        });

        add(loginForm);
    }



}
