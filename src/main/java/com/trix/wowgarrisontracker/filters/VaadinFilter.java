package com.trix.wowgarrisontracker.filters;

import com.trix.wowgarrisontracker.enums.SecurityValues;
import com.trix.wowgarrisontracker.frontEnd.LoginPage;
import com.trix.wowgarrisontracker.services.interfaces.AccountService;
import com.trix.wowgarrisontracker.utils.GeneralUtils;
import com.trix.wowgarrisontracker.utils.JWTutils;
import com.trix.wowgarrisontracker.utils.SecurityUtils;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.VaadinServiceInitListener;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;

@Component
public class VaadinFilter implements VaadinServiceInitListener {

    private GeneralUtils utils;

    private SecurityUtils securityUtils;

    private AccountService accountService;

    private JWTutils jwTutils;

    public VaadinFilter(GeneralUtils utils, SecurityUtils securityUtils, AccountService accountService, JWTutils jwTutils) {
        this.utils = utils;
        this.securityUtils = securityUtils;
        this.accountService = accountService;
        this.jwTutils = jwTutils;
    }

    @Override
    public void serviceInit(ServiceInitEvent serviceInitEvent) {
        serviceInitEvent.getSource().addUIInitListener(uiInitEvent -> {
            final UI ui = uiInitEvent.getUI();
            ui.addBeforeEnterListener(this::authenticateNavigation);
        });
    }

    private void authenticateNavigation(BeforeEnterEvent beforeEnterEvent) {

        Cookie[] cookies = VaadinService.getCurrentRequest().getCookies();
        Cookie jwtCookie = utils.extractCookie(SecurityValues.AUTHRORIZATION.toString(), cookies);
        boolean isUserLoggedIn = securityUtils.isUserLoggedIn();

        if (!LoginPage.class.equals(beforeEnterEvent.getNavigationTarget()) && !isUserLoggedIn) {
            beforeEnterEvent.rerouteTo(LoginPage.class);

        }

        SecurityContextHolder.getContext().setAuthentication(SecurityContextHolder.getContext().getAuthentication());
//
//        if (LoginPage.class.equals(beforeEnterEvent.getNavigationTarget())
//                && isUserLoggedIn) {
//            beforeEnterEvent.rerouteTo(TrackLayout.class);
//        }
    }
}
