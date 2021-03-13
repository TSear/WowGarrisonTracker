package com.trix.wowgarrisontracker.frontEnd.fragments;

import com.github.appreciated.app.layout.addons.notification.DefaultNotificationHolder;
import com.github.appreciated.app.layout.addons.notification.component.NotificationButton;
import com.github.appreciated.app.layout.component.appbar.AppBarBuilder;
import com.github.appreciated.app.layout.component.applayout.LeftLayouts;
import com.github.appreciated.app.layout.component.builder.AppLayoutBuilder;
import com.github.appreciated.app.layout.component.menu.left.builder.LeftAppMenuBuilder;
import com.github.appreciated.app.layout.component.menu.left.items.LeftClickableItem;
import com.github.appreciated.app.layout.component.menu.left.items.LeftNavigationItem;
import com.github.appreciated.app.layout.component.router.AppLayoutRouterLayout;
import com.github.appreciated.app.layout.entity.DefaultBadgeHolder;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.spring.annotation.UIScope;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import com.vaadin.flow.theme.material.Material;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import static com.github.appreciated.app.layout.entity.Section.FOOTER;

@Profile("vaadin")
@StyleSheet("global.css")
@CssImport("./modified-lumo.css")
@Push
@Component
@UIScope
@CssImport(value="./appLayout.css", themeFor = LeftLayouts.LeftResponsive.TAG)
public class MainLayout extends AppLayoutRouterLayout<LeftLayouts.LeftResponsive> {

    private DefaultNotificationHolder notifications = new DefaultNotificationHolder();
    private DefaultBadgeHolder badge = new DefaultBadgeHolder(5);

    public MainLayout() {

        notifications.addClickListener(notification -> {/* ... */});

        LeftNavigationItem menuEntry = new LeftNavigationItem("Menu", VaadinIcon.MENU.create(), View6.class);

        badge.bind(menuEntry.getBadge());




        init(AppLayoutBuilder.get(LeftLayouts.LeftResponsive.class)
                .withTitle("Garrison Tracker")
                .withAppBar(AppBarBuilder.get()
                        .add(new NotificationButton<>(VaadinIcon.BELL, notifications))
                        .build())
                .withAppMenu(LeftAppMenuBuilder.get()
                        .add(new LeftNavigationItem("Track", VaadinIcon.HOME.create(), TrackLayout.class),
                                new LeftNavigationItem("Characters", VaadinIcon.TABLE.create(), View2.class),
                                new LeftNavigationItem("Statistics", VaadinIcon.BAR_CHART, View3.class),
                                new LeftNavigationItem("Auction House", VaadinIcon.MONEY_EXCHANGE, View4.class),
                                new LeftNavigationItem("About", VaadinIcon.INFO, View5.class),
                                new LeftNavigationItem("Contact", VaadinIcon.PHONE, TrackLayout.class))
                        .addToSection(FOOTER,
                                new Hr(),
                                new LeftClickableItem("Log Out", VaadinIcon.SIGN_OUT.create(), clickEvent -> Notification.show("You are trying to log out")))
                        .build())
                .build());

        this.getContent().setClassName("content-box");
    }

    public DefaultNotificationHolder getNotifications() {
        return notifications;
    }

    public DefaultBadgeHolder getBadge() {
        return badge;
    }
}