package com.trix.wowgarrisontracker.frontEnd;

import com.trix.wowgarrisontracker.services.interfaces.AccountService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@CssImport(value = "/css/verification.css")
@Component
@UIScope
@Route(value = "verify")
public class Verification extends FlexLayout implements HasUrlParameter<String> {

    private final AccountService accountService;
    Paragraph paragraph = new Paragraph();

    public Verification(AccountService accountService) {

        this.accountService = accountService;

        configureLayout();

        H2 header = createHeader("Verification");

        Button leaveButton = createLeaveButton("Go to login page");


        add(header);
        add(paragraph);
        add(leaveButton);
    }

    private void configureLayout() {
        setSizeFull();
        setFlexDirection(FlexDirection.COLUMN);
        setAlignItems(Alignment.CENTER);
    }

    private Button createLeaveButton(String text) {
        Button button = new Button(text);
        button.addClickListener(event -> UI.getCurrent().navigate(LoginPage.class));
        return button;
    }

    private H2 createHeader(String text){
        H2 header = new H2(text);
        header.setWidthFull();
        header.getStyle().set("text-align", "center");
        return header;
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
        Location location = event.getLocation();
        Map<String, List<String>> parameters = location.getQueryParameters().getParameters();
        if (parameters.size() > 0 && parameters.containsKey("code")) {
            if (accountService.verify(parameters.get("code").get(0))) {
                paragraph.setText("Your account was activate successfully");
                paragraph.setClassName("success");
            } else {
                paragraph.setText("Activation failed. Your account could already be active");
                paragraph.setClassName("fail");
            }
        } else {
            paragraph.setText("URL query parameter was not found");
            paragraph.setClassName("error");
        }
    }
}
