package com.trix.wowgarrisontracker.frontEnd.views.contact;

import com.trix.wowgarrisontracker.frontEnd.LayoutVariables;
import com.trix.wowgarrisontracker.frontEnd.fragments.MainLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import com.vaadin.flow.spring.annotation.VaadinSessionScope;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.security.PermitAll;

//@StyleSheet("./contact.css")
//@CssImport(value = "./contact.css",themeFor = "vaadin-vertical-layout")
@UIScope
@PermitAll
@PageTitle("Contact")
@Route(value = "contact", layout = MainLayout.class)
public class ContactView extends FlexLayout {


    private final VerticalLayout mainLayout;

    @Value("${about.email}")
    private String email;

    @Value("${about.phonenumber}")
    private String phonenumber;

    public ContactView() {

        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);
        setClassName(LayoutVariables.PRIMARY_BACKGROUND);

        VerticalLayout wrapperForBorder = new VerticalLayout();
        wrapperForBorder.setWidth("auto");
        wrapperForBorder.setClassName("border-wrap");
        add(wrapperForBorder);

        mainLayout = new VerticalLayout();
        mainLayout.setAlignItems(Alignment.CENTER);
        mainLayout.getStyle().set("padding", "3.5em");
        mainLayout.getStyle().set("background", "#252422");
        wrapperForBorder.add(mainLayout);


//        createInfoBox("Second Phone Number: ", "placeholder");
    }

    @PostConstruct
    private void init(){
        createInfoBox("E-mail: ", email);
        createInfoBox("Phone Number: ", phonenumber);
    }

    private void createInfoBox(String s, String labelValue) {
        FlexLayout infoBox = new FlexLayout();
        infoBox.setClassName("info-box");
        infoBox.setWidthFull();
        infoBox.setFlexWrap(FlexWrap.WRAP);
        infoBox.setAlignContent(ContentAlignment.CENTER);
        infoBox.setJustifyContentMode(JustifyContentMode.CENTER);
        Label text = new Label(s);
        Label value = new Label(labelValue);
        text.setClassName("text");
        value.setClassName("value");
        infoBox.add(text, value);
        mainLayout.add(infoBox);

    }


}
