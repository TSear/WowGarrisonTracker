package com.trix.wowgarrisontracker.frontEnd;

import com.trix.wowgarrisontracker.model.Server;
import com.trix.wowgarrisontracker.pojos.RegisterPojo;
import com.trix.wowgarrisontracker.services.interfaces.AccountService;
import com.trix.wowgarrisontracker.services.interfaces.ServerService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@UIScope
@Component
@Route(value = "register")
public class RegisterPage extends VerticalLayout {

    private final AccountService accountService;

    private final List<Server> servers;

    private final Binder<RegisterPojo> pojoBinder = new Binder<>();

    public RegisterPage(AccountService accountService, ServerService serverService) {
        this.accountService = accountService;
        servers = serverService.getServers();
    }

    @PostConstruct
    private void init() {

        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setClassName("background");
        servers.sort(Comparator.comparing(Server::getName));

        H2 registerHeader = new H2("Create Account");
        registerHeader.getStyle().set("text-align", "center");

        FormLayout formLayout = new FormLayout();
        formLayout.setClassName("register-form");
        formLayout.addClassName("box-shadow");
        formLayout.getStyle().set("margin", "auto");
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1));
        formLayout.setWidth("35%");

        TextField loginField = new TextField();
        loginField.setLabel("Login");
        pojoBinder.forField(loginField)
                .withValidator(new StringLengthValidator("Login must be between 4 - 30 characters", 4, 30))
                .withValidator(s -> accountService.findUserByUsername(s) == null, "Login taken")
                .bind(RegisterPojo::getLogin, RegisterPojo::setLogin);

        TextField emailField = new TextField();
        emailField.setLabel("Email");
        pojoBinder.forField(emailField)
                .withValidator(new EmailValidator("This is not valid email"))
                .bind(RegisterPojo::getEmail, RegisterPojo::setEmail);


        PasswordField passwordField = new PasswordField();
        PasswordField repeatedPasswordField = new PasswordField();

        passwordField.addValueChangeListener(event -> checkIfValid(passwordField, repeatedPasswordField));

        repeatedPasswordField.setLabel("Repeat Password");
        repeatedPasswordField.addValueChangeListener(event -> checkIfValid(passwordField, repeatedPasswordField));

        passwordField.setLabel("Password");
        pojoBinder.forField(passwordField)
                .withValidator(new StringLengthValidator("Password must be between 8-30 characters", 8, 30))
                .withValidator(s -> s.equals(repeatedPasswordField.getValue()), "Passwords do not match")
                .bind(RegisterPojo::getPassword, RegisterPojo::setPassword);


        ComboBox<String> region = new ComboBox<>();
        region.setLabel("Pick Region");
        region.setItems(Arrays.asList("All", "EU", "US"));
        region.setValue("EU");
        region.setAllowCustomValue(false);


        ComboBox<Server> server = new ComboBox<>();
        server.setLabel("Pick Server");
        server.setItemLabelGenerator(Server::toString);
        server.setItems(servers.stream().filter(server1 -> server1.getRegion().equalsIgnoreCase("eu")).collect(Collectors.toList()));
        pojoBinder.forField(server)
                .withValidator(Objects::nonNull, "Pick server")
                .bind(RegisterPojo::getServer, RegisterPojo::setServer);

        region.addValueChangeListener(event -> {

            if (region.getValue().equalsIgnoreCase("All"))
                server.setItems(servers);
            else
                server.setItems(servers.stream()
                        .filter(server1 -> event.getValue().toLowerCase().equals(server1.getRegion())).collect(Collectors.toList()));
        });


        formLayout.add(registerHeader, loginField, emailField, passwordField, passwordField, repeatedPasswordField, region, server);

        HorizontalLayout buttonsLayout = new HorizontalLayout();
        buttonsLayout.getStyle().set("margin-top", "1em");

        Button submitButton = new Button("Submit");

        submitButton.addClickListener(event -> {
            RegisterPojo pojo = new RegisterPojo();
            try {
                pojoBinder.writeBean(pojo);
                accountService.createAccount(pojo);
                UI.getCurrent().navigate(LoginPage.class);
            } catch (ValidationException e) {
                System.out.println(e.getMessage());
            }

        });

        Button cancelButton = new Button("Cancel");
        cancelButton.addClassName("secondary-button");
        cancelButton.addClickListener(event -> UI.getCurrent().navigate(LoginPage.class));

        buttonsLayout.add(submitButton, cancelButton);
        buttonsLayout.setFlexGrow(1, submitButton);

        formLayout.add(buttonsLayout);


        add(formLayout);
    }

    private void checkIfValid(PasswordField password, PasswordField repeated) {
        if (password.getValue().equals(repeated.getValue())) {
            repeated.setInvalid(false);
            password.setInvalid(false);
        } else {
            repeated.setInvalid(true);
            repeated.setErrorMessage("Passwords do not match");
            password.setInvalid(true);
        }
    }

}
