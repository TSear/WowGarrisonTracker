package com.trix.wowgarrisontracker.frontEnd.fragments;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.spring.annotation.UIScope;

@UIScope
public class AddButton extends Button {

    public AddButton(String text, Dialog dialog) {

        setText(text);
        addClickListener(event -> dialog.open());

    }

}
