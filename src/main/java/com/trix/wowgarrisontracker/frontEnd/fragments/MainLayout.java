package com.trix.wowgarrisontracker.frontEnd.fragments;

import com.trix.wowgarrisontracker.frontEnd.*;
import com.trix.wowgarrisontracker.frontEnd.views.auctionHouse.AuctionHouseView;
import com.trix.wowgarrisontracker.frontEnd.views.cardsOfOmen.CardsOfOmenView;
import com.trix.wowgarrisontracker.frontEnd.views.characters.CharactersView;
import com.trix.wowgarrisontracker.frontEnd.views.contact.ContactView;
import com.trix.wowgarrisontracker.frontEnd.views.resources.ResourcesView;
import com.trix.wowgarrisontracker.frontEnd.views.settings.SettingsView;
import com.trix.wowgarrisontracker.frontEnd.views.statistics.StatisticsView;
import com.trix.wowgarrisontracker.services.implementation.SecurityService;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.spring.annotation.UIScope;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@CssImport(value = "/css/globalVariables.css")
@CssImport(value = "/css/global.css", themeFor = "vaadin-button")
@CssImport(value = "/css/MainLayout.css", themeFor = "vaadin-app-layout")
@CssImport(value = "/css/MainLayout.css")
@UIScope
//TODO Add annotations configs for security
public class MainLayout extends AppLayout {


    public MainLayout() {

        H2 logoText = new H2("Garrison Tracker");
        addToNavbar(new DrawerToggle(), logoText);
        setDrawerOpened(true);


        generateTabs();

    }

    private void generateTabs() {

        Tabs tabs = new Tabs();
        tabs.setOrientation(Tabs.Orientation.VERTICAL);

        Button logout = new Button("Log Out");
        logout.addClickListener(buttonClickEvent -> SecurityService.logout());
        logout.setClassName("logout-button");

        List<Tab> listOfTabs = new ArrayList<>(Arrays.asList(
                new Tab(VaadinIcon.LINES_LIST.create(), new RouterLink("Track", ResourcesView.class)),
                new Tab(VaadinIcon.MONEY.create(), new RouterLink("Cards of Omen", CardsOfOmenView.class)),
                new Tab(VaadinIcon.GROUP.create(), new RouterLink("Characters", CharactersView.class)),
                new Tab(VaadinIcon.CHART.create(), new RouterLink("Statistics", StatisticsView.class)),
                new Tab(VaadinIcon.MONEY_EXCHANGE.create(), new RouterLink("Auction House", AuctionHouseView.class)),
                new Tab(VaadinIcon.PHONE.create(), new RouterLink("Contact", ContactView.class)),
                new Tab(VaadinIcon.COGS.create(), new RouterLink("Settings", SettingsView.class)),
                new Tab(VaadinIcon.EXIT.create(), logout))
        );

        listOfTabs.forEach(tab -> {
            tab.setClassName(LayoutVariables.DRAWER_TAB);
            tabs.add(tab);
        });

        listOfTabs.get(listOfTabs.size()-1).addClassName(LayoutVariables.LOG_OUT);


        addToDrawer(tabs);
    }

}
