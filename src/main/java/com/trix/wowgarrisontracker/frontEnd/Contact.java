package com.trix.wowgarrisontracker.frontEnd;

import com.trix.wowgarrisontracker.frontEnd.fragments.MainLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

//@StyleSheet("./contact.css")
//@CssImport(value = "./contact.css",themeFor = "vaadin-vertical-layout")
@Route(value = "contact", layout = MainLayout.class)
public class Contact extends FlexLayout {


    private final VerticalLayout mainLayout;

    public Contact() {

        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);

        VerticalLayout wrapperForBorder = new VerticalLayout();
        wrapperForBorder.setWidth("auto");
        wrapperForBorder.setClassName("border-wrap");
        add(wrapperForBorder);

        mainLayout = new VerticalLayout();
        mainLayout.setAlignItems(Alignment.CENTER);
        mainLayout.getStyle().set("padding", "3.5em");
        mainLayout.getStyle().set("background", "#252422");
        wrapperForBorder.add(mainLayout);

        createInfoBox("E-mail: ", "placeholder");
        createInfoBox("Phone Number: ", "placeholder");
        createInfoBox("Second Phone Number: ", "placeholder");
    }

    private void createInfoBox(String s, String labelValue) {
        HorizontalLayout infoBox = new HorizontalLayout();
        infoBox.setClassName("info-box");
        infoBox.setWidthFull();
        Label text = new Label(s);
        Label value = new Label(labelValue);
        text.setClassName("text");
        value.setClassName("value");
        infoBox.add(text, value);
        mainLayout.add(infoBox);

    }


}
