package com.trix.wowgarrisontracker.frontEnd.components;

import com.trix.wowgarrisontracker.pojos.Credentials;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep.LabelsPosition.*;

public class NewPasswordField extends CustomField<String> {

    private final Credentials credentials;
    private final PasswordEncoder passwordEncoder;
    private final PasswordField oldPasswordField;
    private final PasswordField newPasswordField;
    private final PasswordField newPasswordRepeatField;

    public NewPasswordField(Credentials credentials, PasswordEncoder passwordEncoder) {

        this.credentials = credentials;
        this.passwordEncoder = passwordEncoder;

        FormLayout formLayout = new FormLayout();
        formLayout.setWidthFull();
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0px", 1, TOP),
                new FormLayout.ResponsiveStep("300px",1, ASIDE));

        oldPasswordField = new PasswordField();
        oldPasswordField.setWidthFull();

        newPasswordField = new PasswordField();
        newPasswordField.setWidthFull();
        

        newPasswordRepeatField = new PasswordField();
        newPasswordRepeatField.setWidthFull();

        formLayout.addFormItem(oldPasswordField, "Old password");
        formLayout.addFormItem(newPasswordField, "New password");
        formLayout.addFormItem(newPasswordRepeatField, "Repeat new password");

        add(formLayout);
    }
    

    @Override
    protected String generateModelValue() {

        oldPasswordField.setInvalid(false);
        newPasswordField.setInvalid(false);
        newPasswordRepeatField.setInvalid(false);

        boolean isOldPasswordMatching = passwordEncoder.matches(oldPasswordField.getValue(), credentials.getOldPassword());
        boolean isNewPasswordMatching = newPasswordField.getValue().equals(newPasswordRepeatField.getValue());

        if (isOldPasswordMatching && isNewPasswordMatching) {
            return newPasswordField.getValue();
        }

        if (!isOldPasswordMatching) {
            oldPasswordField.setErrorMessage("Old password is incorrect");
            oldPasswordField.setInvalid(true);
        }

        if (!newPasswordField.getValue().equals(newPasswordRepeatField.getValue())) {
            newPasswordField.setErrorMessage("Passwords does not match");
            newPasswordRepeatField.setErrorMessage("Passwords does not match");
            newPasswordField.setInvalid(true);
            newPasswordRepeatField.setInvalid(true);
        }
        return null;
    }

    @Override
    protected void setPresentationValue(String newPresentationValue) {

    }
}
