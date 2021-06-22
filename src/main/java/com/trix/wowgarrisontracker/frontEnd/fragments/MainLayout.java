package com.trix.wowgarrisontracker.frontEnd.fragments;

import com.trix.wowgarrisontracker.frontEnd.*;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.spring.annotation.UIScope;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@CssImport(value = "/css/MainLayout.css", themeFor = "vaadin-app-layout")
@CssImport(value = "/css/MainLayout.css")
@UIScope
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

        List<Tab> listOfTabs = new ArrayList<>(Arrays.asList(
                new Tab(VaadinIcon.LINES_LIST.create(), new RouterLink("Track", TrackLayout.class)),
                new Tab(VaadinIcon.GROUP.create(), new RouterLink("Characters", CharactersLayout.class)),
                new Tab(VaadinIcon.CHART.create(), new RouterLink("Statistics", Statistics.class)),
                new Tab(VaadinIcon.MONEY.create(), new RouterLink("Auction House", AuctionHouse.class)),
                new Tab(VaadinIcon.PHONE.create(), new RouterLink("Contact", Contact.class)),
                new Tab(VaadinIcon.COGS.create(), new RouterLink("Settings", Settings.class)),
                new Tab(VaadinIcon.EXIT.create(), new Anchor("logout", "Log Out")))
        );

        listOfTabs.forEach(tab -> {
            tab.setClassName(LayoutVariables.DRAWER_TAB);
            tabs.add(tab);
        });

        listOfTabs.get(listOfTabs.size()-1).addClassName(LayoutVariables.LOG_OUT);


        addToDrawer(tabs);
    }

}
