package com.trix.wowgarrisontracker.frontEnd.views.characters;

import com.trix.wowgarrisontracker.frontEnd.interfaces.Refreshable;
import com.trix.wowgarrisontracker.pojos.AccountCharacterForm;
import com.trix.wowgarrisontracker.pojos.AccountCharacterPojo;
import com.trix.wowgarrisontracker.services.interfaces.AccountCharacterService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.spring.annotation.UIScope;
import com.vaadin.flow.spring.annotation.VaadinSessionScope;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UIScope
public class NewCharacterDialog extends Dialog {

    private final AccountCharacterService characterService;
    private final Binder<AccountCharacterForm> binder = new Binder<>();
    private final Long id;
    private final Refreshable parentData;
    private AccountCharacterForm accountCharacterForm;

    public NewCharacterDialog(AccountCharacterService characterService, Long id, Refreshable parentData) {
        this.characterService = characterService;
        this.id = id;
        this.parentData = parentData;
        this.accountCharacterForm = new AccountCharacterForm();

        init();
    }


    private void init() {

        configureDialog();

        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.getStyle().set("background", "#252422");

        FormLayout formLayout = createFormLayout();

        HorizontalLayout buttonLayout = createButtonLayout();

        TextField characterNameField = createCharacterNameTextField();

        Button createButton = createAddNewCharacterButton(buttonLayout);

        Button cancelButton = createCancelButton();

        binder.readBean(accountCharacterForm);

        buttonLayout.add(createButton, cancelButton);

        mainLayout.add(formLayout);
        mainLayout.add(buttonLayout);

        formLayout.add(characterNameField);

        add(mainLayout);
    }

    private Button createCancelButton() {
        Button cancelButton = new Button("Cancel");
        cancelButton.addClickListener(event -> this.close());
        cancelButton.addClassName("secondary-button");
        return cancelButton;
    }

    private Button createAddNewCharacterButton(HorizontalLayout horizontalLayout) {
        Button createButton = new Button("Create");
        horizontalLayout.setFlexGrow(1, createButton);

        createButton.addClickListener(event -> {
            try {
                binder.writeBean(accountCharacterForm);
                saveAndCleanDialog();

            } catch (ValidationException e) {
                log.info("Validation failed: " + NewCharacterDialog.class);
            }
        });
        return createButton;
    }

    private void saveAndCleanDialog() {
        accountCharacterForm.setAccountId(id);

        //TODO need to handle saving in another way
        characterService.save(accountCharacterForm);

        accountCharacterForm = new AccountCharacterForm();
        binder.readBean(accountCharacterForm);
        parentData.refresh();
        this.close();
    }

    private HorizontalLayout createButtonLayout() {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setWidthFull();
        buttonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        return buttonLayout;
    }

    private FormLayout createFormLayout() {
        FormLayout formLayout = new FormLayout();
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP));
        formLayout.setWidthFull();
        return formLayout;
    }

    private TextField createCharacterNameTextField() {
        TextField characterNameField = new TextField("Character Name");
        characterNameField.setPlaceholder("Hokmurzok");
        characterNameField.setMaxLength(100);
        binder.forField(characterNameField)
                .withValidator(validate ->
                        !characterService.isNameTaken(id, characterNameField.getValue()), "Name is taken")
                .withValidator(new StringLengthValidator("Character name must be between 1 and 100 characters", 1, 100))
                .bind(AccountCharacterForm::getAccountCharacterName, AccountCharacterForm::setAccountCharacterName);
        return characterNameField;
    }

    private void configureDialog() {
        this.setCloseOnEsc(true);
        this.setDraggable(true);
        this.setCloseOnOutsideClick(true);

    }

}
