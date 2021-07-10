package com.trix.wowgarrisontracker.frontEnd.views.settings;

import com.trix.wowgarrisontracker.frontEnd.LayoutVariables;
import com.trix.wowgarrisontracker.frontEnd.components.CustomHr;
import com.trix.wowgarrisontracker.frontEnd.fragments.MainLayout;
import com.trix.wowgarrisontracker.frontEnd.interfaces.Refreshable;
import com.trix.wowgarrisontracker.frontEnd.views.auctionHouse.AuctionHouseView;
import com.trix.wowgarrisontracker.model.Options;
import com.trix.wowgarrisontracker.services.interfaces.AccountService;
import com.trix.wowgarrisontracker.services.interfaces.OptionsService;
import com.trix.wowgarrisontracker.services.interfaces.ServerService;
import com.trix.wowgarrisontracker.utils.GeneralUtils;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.PostConstruct;
import javax.annotation.security.PermitAll;
import java.util.ArrayList;
import java.util.List;

@CssImport(value = "/css/settings.css", themeFor = "vaadin-form-layout")
@UIScope
@PermitAll
@PageTitle("Settings")
@Route(value = "settings", layout = MainLayout.class)
public class SettingsView extends VerticalLayout {

    private final Binder<Options> optionsBinder;
    private final Options options;
    private final ServerService serverService;
    private final OptionsService optionsService;
    private final PasswordEncoder passwordEncoder;
    private final AccountService accountService;

    private final CredentialsSettings credentialsSettings;

    private final List<Refreshable> refreshableList;

    public SettingsView(ServerService serverService,
                        AuctionHouseView auctionHouse,
                        OptionsService optionsService,
                        PasswordEncoder passwordEncoder,
                        AccountService accountService,
                        CredentialsSettings credentialsSettings) {

        this.optionsService = optionsService;
        this.serverService = serverService;
        this.refreshableList = new ArrayList<>();
        this.refreshableList.add(auctionHouse);
        this.passwordEncoder = passwordEncoder;
        this.accountService = accountService;
        this.credentialsSettings = credentialsSettings;

        this.optionsBinder = new Binder<>();
        this.options = GeneralUtils.getUserSettings();
    }

    @PostConstruct
    private void init() {

        configureFrame();

        ServerSettings serverSettings = new ServerSettings(serverService, this,optionsService);

        add(serverSettings);
        add(new CustomHr());
        add(credentialsSettings);

        optionsBinder.readBean(options);

    }

    private void configureFrame() {
        this.setWidthFull();
        this.setClassName("background");
        addClassName(LayoutVariables.PRIMARY_BACKGROUND);
    }

    public void refreshAll(){
        refreshableList.forEach(Refreshable::refresh);
    }
}
