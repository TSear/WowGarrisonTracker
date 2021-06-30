package com.trix.wowgarrisontracker.frontEnd.components;

import com.vaadin.flow.component.html.H2;

public class CenteredHeader extends H2 {

    public CenteredHeader(String headerText) {
        this.setText(headerText);
        setWidthFull();
        getStyle().set("text-align", "center");
    }
}
