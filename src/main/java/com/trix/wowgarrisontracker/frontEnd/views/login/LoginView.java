package com.trix.wowgarrisontracker.frontEnd.views.login;

import ch.qos.logback.core.Layout;
import com.trix.wowgarrisontracker.frontEnd.LayoutVariables;
import com.trix.wowgarrisontracker.utils.BlizzardRequestUtils;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.spring.annotation.UIScope;
import com.vaadin.flow.spring.annotation.VaadinSessionScope;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Arrays;


@UIScope
@Profile("vaadin")
@AnonymousAllowed
@PageTitle("Log In")
@Route(value = "login")
public class LoginView extends FlexLayout implements BeforeEnterObserver {


    private final LoginForm loginForm = new LoginForm();
    private final BlizzardRequestUtils requestUtils;
    private final VerticalLayout verticalLayout = new VerticalLayout();

    public LoginView(Environment environment) {
        this.requestUtils = new BlizzardRequestUtils();
        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);
        addClassName("background");
        addClassName(LayoutVariables.PRIMARY_BACKGROUND);

        verticalLayout.addClassName("box-shadow");

        loginForm.setAction("login");

        if (Arrays.asList(environment.getActiveProfiles()).contains(LayoutVariables.DEMO)){

            H3 header = new H3("Example credentials");
            header.setWidthFull();
            header.getStyle().set("text-align","center");

            Label login = new Label("Login: login1");
            login.setClassName(LayoutVariables.LABEL);
            Label password = new Label("Password: password1");
            password.setClassName(LayoutVariables.LABEL);

            verticalLayout.add(header, login, password);

        }


        verticalLayout.add(loginForm);
        verticalLayout.setSizeUndefined();

        Anchor registerLink = new Anchor("register", "Don't have account?");
        registerLink.addClassName("register-link");
        registerLink.setWidthFull();

        verticalLayout.add(registerLink);

        add(verticalLayout);
    }


    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (event.getLocation().getQueryParameters().getParameters().containsKey("error")) {
            loginForm.setError(true);
        }
    }
}
