package com.trix.wowgarrisontracker.filters;

import com.trix.wowgarrisontracker.frontEnd.LoginPage;
import com.trix.wowgarrisontracker.utils.SecurityUtils;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import org.springframework.stereotype.Component;

@Component
public class VaadinFilter implements VaadinServiceInitListener {


    public VaadinFilter() {
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

    }
}
