package com.trix.wowgarrisontracker.frontEnd;

import com.trix.wowgarrisontracker.frontEnd.fragments.MenuBarFragment;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.material.Material;
import org.springframework.context.annotation.Profile;

@StyleSheet("menuBar.css")
@Profile("vaadin")
@Route("infoPage")
public class InfoPage extends VerticalLayout {

    public InfoPage() {

        setMargin(false);
        setPadding(false);
        MenuBarFragment menuBar = new MenuBarFragment();
        this.add(menuBar);

    }
}
