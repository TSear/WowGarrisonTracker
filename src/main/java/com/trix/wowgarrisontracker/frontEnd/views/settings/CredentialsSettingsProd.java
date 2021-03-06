package com.trix.wowgarrisontracker.frontEnd.views.settings;

import com.trix.wowgarrisontracker.frontEnd.LayoutVariables;
import com.trix.wowgarrisontracker.frontEnd.components.CenteredHeader;
import com.trix.wowgarrisontracker.frontEnd.components.NewPasswordField;
import com.trix.wowgarrisontracker.frontEnd.components.SaveButton;
import com.trix.wowgarrisontracker.frontEnd.interfaces.BinderSave;
import com.trix.wowgarrisontracker.model.Account;
import com.trix.wowgarrisontracker.pojos.Credentials;
import com.trix.wowgarrisontracker.services.interfaces.AccountService;
import com.trix.wowgarrisontracker.utils.GeneralUtils;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.PropertyId;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import static com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep.LabelsPosition.ASIDE;
import static com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep.LabelsPosition.TOP;

@Component
@UIScope
@Profile("!demo")
public class CredentialsSettingsProd extends CredentialsSettings {

    private final BeanValidationBinder<Credentials> accountBinder;
    private final FormLayout formLayout;
    private final PasswordEncoder passwordEncoder;
    private final AccountService accountService;
    private final Credentials credentials;

    @PropertyId("email")
    private EmailField emailField;

    @PropertyId("newPassword")
    private NewPasswordField newPasswordField;


    public CredentialsSettingsProd(PasswordEncoder passwordEncoder,
                                   AccountService accountService) {
        this.accountBinder = new BeanValidationBinder<>(Credentials.class);
        this.formLayout = new FormLayout();
        this.passwordEncoder = passwordEncoder;
        this.accountService = accountService;

        credentials = new Credentials(GeneralUtils.getUserSettings().getAccount());

        init();
    }

    private void init() {

        configureFormLayout();

        CenteredHeader header = new CenteredHeader("Account Credentials");

        emailField = new EmailField();
        emailField.setWidthFull();

        newPasswordField = new NewPasswordField(credentials, passwordEncoder);
        accountBinder.forField(newPasswordField)
                .withNullRepresentation("")
                .withValidator(new StringLengthValidator("Password must be between 8-30 chars", 8, 30))
                .bind(credentials -> "", Credentials::setNewPassword);

        SaveButton saveButton = new SaveButton(saveLambda());

        accountBinder.bindInstanceFields(this);
        accountBinder.readBean(credentials);

        add(header);

        formLayout.addFormItem(emailField, "Email");

        formLayout.add(newPasswordField);

        formLayout.add(saveButton);

        add(formLayout);

    }

    private BinderSave saveLambda() {
        return () -> {

            if (accountBinder.hasChanges()) {

                Account account = GeneralUtils.getUserSettings().getAccount();
                accountBinder.writeBean(credentials);

                if (credentials.getNewPassword() != null && credentials.getNewPassword().length() >= 8) {
                    account.setPassword(passwordEncoder.encode(credentials.getNewPassword()));
                }
                if (credentials.getEmail() != null) {
                    account.setEmail(credentials.getEmail());
                }

                accountService.save(account);
                return true;
            }
            return false;
        };
    }

    private void configureFormLayout() {
        formLayout.setWidthFull();
        formLayout.setMaxWidth(LayoutVariables.FORM_MAX_WIDTH);
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0px", 1, TOP),
                new FormLayout.ResponsiveStep("300px",1, ASIDE));
    }


}
