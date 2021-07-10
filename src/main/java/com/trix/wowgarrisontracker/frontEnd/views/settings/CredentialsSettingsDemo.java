package com.trix.wowgarrisontracker.frontEnd.views.settings;

import com.trix.wowgarrisontracker.frontEnd.LayoutVariables;
import com.trix.wowgarrisontracker.frontEnd.components.CenteredHeader;
import com.trix.wowgarrisontracker.frontEnd.components.NewPasswordField;
import com.trix.wowgarrisontracker.frontEnd.components.SaveButton;
import com.trix.wowgarrisontracker.pojos.Credentials;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import static com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep.LabelsPosition.ASIDE;
import static com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep.LabelsPosition.TOP;

@Component
@UIScope
@Profile("demo")
public class CredentialsSettingsDemo extends CredentialsSettings{


    private final PasswordEncoder passwordEncoder;

    public CredentialsSettingsDemo(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    private void init(){

        FormLayout formLayout = new FormLayout();
        formLayout.setWidthFull();
        formLayout.setMaxWidth(LayoutVariables.FORM_MAX_WIDTH);
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0px", 1, TOP),
                new FormLayout.ResponsiveStep("300px",1, ASIDE));

        CenteredHeader header = new CenteredHeader("Account Credentials");
        Paragraph paragraph = new Paragraph("You cannot change credentials in demo version");
        paragraph.setWidthFull();
        paragraph.getStyle().set("text-align","center");

        EmailField emailField = new EmailField();
        emailField.setEnabled(false);
        emailField.setWidthFull();

        Credentials credentials = new Credentials();
        credentials.setEmail("");
        credentials.setOldPassword("");
        credentials.setNewPassword("");


        NewPasswordField newPasswordField = new NewPasswordField(credentials, passwordEncoder);
        newPasswordField.setEnabled(false);

        SaveButton saveButton = new SaveButton(() -> false);
        saveButton.setEnabled(false);

        add(header, paragraph);

        formLayout.addFormItem(emailField, "Email");

        formLayout.add(newPasswordField);

        formLayout.add(saveButton);

        add(formLayout);
    }
}
