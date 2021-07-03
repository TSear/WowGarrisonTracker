package com.trix.wowgarrisontracker.frontEnd.views.about;

import com.trix.wowgarrisontracker.frontEnd.LayoutVariables;
import com.trix.wowgarrisontracker.frontEnd.fragments.About;
import com.trix.wowgarrisontracker.frontEnd.fragments.MainLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;

import javax.annotation.security.PermitAll;

@PermitAll
@UIScope
@Route(value = "about", layout = MainLayout.class)
public class AboutView extends VerticalLayout {

    public AboutView() {
        setWidthFull();
        setClassName(LayoutVariables.PRIMARY_BACKGROUND);
        add(new About());
    }
}
