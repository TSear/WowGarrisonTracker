package com.trix.wowgarrisontracker.filters;

import com.trix.wowgarrisontracker.enums.SecurityValues;
import com.trix.wowgarrisontracker.frontEnd.LoginPage;
import com.trix.wowgarrisontracker.services.interfaces.AccountService;
import com.trix.wowgarrisontracker.utils.SecurityUtils;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.VaadinServiceInitListener;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;

@Component
public class VaadinFilter implements VaadinServiceInitListener {


    private SecurityUtils securityUtils;

    private AccountService accountService;


    public VaadinFilter(SecurityUtils securityUtils, AccountService accountService) {
        this.securityUtils = securityUtils;
        this.accountService = accountService;
    }

    @Override
    public void serviceInit(ServiceInitEvent serviceInitEvent) {
        serviceInitEvent.getSource().addUIInitListener(uiInitEvent -> {
            final UI ui = uiInitEvent.getUI();
            ui.addBeforeEnterListener(this::authenticateNavigation);
        });
    }

    private void authenticateNavigation(BeforeEnterEvent beforeEnterEvent) {

        boolean isUserLoggedIn = SecurityUtils.isUserLoggedIn();

        if (!LoginPage.class.equals(beforeEnterEvent.getNavigationTarget()) && !isUserLoggedIn) {
            beforeEnterEvent.rerouteTo(LoginPage.class);

        }
//
//        if (LoginPage.class.equals(beforeEnterEvent.getNavigationTarget())
//                && isUserLoggedIn) {
//            beforeEnterEvent.rerouteTo(TrackLayout.class);
//        }
    }
}
