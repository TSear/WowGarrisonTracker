package com.trix.wowgarrisontracker.frontEnd;

import com.trix.wowgarrisontracker.utils.BlizzardRequestUtils;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import com.vaadin.flow.spring.annotation.VaadinSessionScope;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;


@UIScope
@Profile("vaadin")
@Route(value = "login")
public class LoginPage extends FlexLayout implements BeforeEnterObserver {


    private final LoginForm loginForm = new LoginForm();
    private final BlizzardRequestUtils requestUtils;
    private final VerticalLayout verticalLayout = new VerticalLayout();

    public LoginPage() {
        this.requestUtils = new BlizzardRequestUtils();
        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);
        addClassName("background");
        addClassName(LayoutVariables.PRIMARY_BACKGROUND);

        verticalLayout.addClassName("box-shadow");

        loginForm.setAction("login");
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
