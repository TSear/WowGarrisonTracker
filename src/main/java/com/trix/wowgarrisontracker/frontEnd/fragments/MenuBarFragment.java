package com.trix.wowgarrisontracker.frontEnd.fragments;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class MenuBarFragment extends HorizontalLayout {

    ContextMenu menu = new ContextMenu();

    public MenuBarFragment() {


//Configure layout
        this.setClassName("menu-bar");
        this.setHeight("100px");
        this.setWidth("100%");
        this.setPadding(true);
        this.setAlignItems(Alignment.CENTER);
        this.setJustifyContentMode(JustifyContentMode.CENTER);

        Button overflowButton = new Button(new Icon(VaadinIcon.ELLIPSIS_DOTS_H));
        overflowButton.addClassName("overflow-button");

        menu.setTarget(overflowButton);
        menu.setOpenOnClick(true);
        menu.setClassName("context-menu");
        add(menu);

        //Logo Label
        Label labelLogo = new Label("Garrison Tracker");
        labelLogo.getStyle().set("font-size", "20px");
        labelLogo.getStyle().set("font-weight", "bold");
        labelLogo.setWidth("10%");
        labelLogo.setMinWidth("180px");
        labelLogo.getStyle().set("margin-right", "10%");
        add(labelLogo);


        //Anchors
        configureAnchor("/track", "Track");
        configureAnchor("/characters", "Characters");
        configureAnchor("/statistics", "Statistics");
        configureAnchor("/auctionHouse", "Auction House");
        configureAnchor("/about", "About");
        configureAnchor("/contact", "Contact");

        menu.add(new Hr());

        Anchor anchorLogOut = new Anchor("/logOut", "Log Out");
        anchorLogOut.getStyle().set("margin-left", "auto");
        add(anchorLogOut);

        Anchor contextLogOut = new Anchor("/logOut", "Log Out");
        contextLogOut.setWidthFull();
        menu.addItem(contextLogOut);

        add(overflowButton);


    }

    private void configureAnchor(String link, String anchorText) {
        Anchor anchorMenu = new Anchor(link, anchorText);
        anchorMenu.getStyle().set("margin-right", "2%");
        anchorMenu.getStyle().set("text-align", "center");
        anchorMenu.setClassName("menu-button");
        add(anchorMenu);

        Anchor anchorContextMenu = new Anchor(link, anchorText);
        anchorContextMenu.setWidthFull();
        anchorContextMenu.setClassName("context-menu-item");
        anchorContextMenu.setWidthFull();

        menu.addItem(anchorContextMenu);
    }

}

