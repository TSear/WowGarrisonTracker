package com.trix.wowgarrisontracker.frontEnd;

import com.mysql.cj.result.IntegerValueFactory;
import com.trix.wowgarrisontracker.model.AccountCharacter;
import com.trix.wowgarrisontracker.model.Entry;
import com.trix.wowgarrisontracker.pojos.AccountCharacterPojo;
import com.trix.wowgarrisontracker.pojos.EntryPojo;
import com.trix.wowgarrisontracker.services.interfaces.AccountCharacterService;
import com.trix.wowgarrisontracker.utils.GeneralUtils;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.validator.RangeValidator;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.List;


@Component
@UIScope
public class EntryFormDialog extends Dialog {

    GeneralUtils utils;
    AccountCharacterService accountCharacterService;
    Binder<EntryPojo> entryPojoBinder = new Binder<>();

    public EntryFormDialog( @Autowired AccountCharacterService accountCharacterService, @Autowired GeneralUtils utils) {

        this.utils = utils;
        this.accountCharacterService = accountCharacterService;
    }

    @PostConstruct
    private void generateDialogBox() {
        this.configureDialog();

        EntryPojo entryPojo = new EntryPojo();
        entryPojoBinder.readBean(entryPojo);

        Long id = utils.getId(VaadinService.getCurrentRequest().getCookies());

        VerticalLayout mainDialogBoxLayout = createMainDialogLayout();

        FormLayout entryFormLayout = createEntryFormLayout();

        List<AccountCharacter> accountCharacterList = accountCharacterService.findAllByAccountId(id);

        ComboBox<AccountCharacter> accountCharacters = createAccountCharacterPojoComboBox(accountCharacterList);
        entryPojoBinder.forField(accountCharacters)
                .bind(EntryPojo::getAccountCharacter, EntryPojo::setAccountCharacter);

        IntegerField garrisonResources = new IntegerField("Garrison Resources");
        entryPojoBinder.forField(garrisonResources)
                    .bind(EntryPojo::getGarrisonResources,EntryPojo::setGarrisonResources);

        IntegerField warPaint = new IntegerField("War Paint");
        entryPojoBinder.forField(warPaint)
                .bind(EntryPojo::getWarPaint,EntryPojo::setWarPaint);

        Label entryDate = new Label("Entry Date: " + LocalDate.now().toString());

        entryFormLayout.add(accountCharacters);
        entryFormLayout.add(garrisonResources);
        entryFormLayout.add(warPaint);
        entryFormLayout.add(entryDate);

        mainDialogBoxLayout.add(entryFormLayout);

        HorizontalLayout buttonLayout = createButtonLayout();

        Button confirmButton = createCreateEntryButton(this, "Create Entry");

        Button cancelButton = createCancelButton(this, "Cancel");

        buttonLayout.add(confirmButton, cancelButton);
        mainDialogBoxLayout.add(buttonLayout);

        this.add(mainDialogBoxLayout);
        mainDialogBoxLayout.setFlexGrow(1, buttonLayout);
        mainDialogBoxLayout.setAlignItems(FlexComponent.Alignment.STRETCH);

    }

    private Button createCancelButton(Dialog dialog, String cancel) {
        Button cancelButton = new Button(cancel);
        cancelButton.addClickListener(event -> {
            dialog.close();
        });
        return cancelButton;
    }

    private Button createCreateEntryButton(Dialog dialog, String s) {
        Button confirmButton = new Button(s);
        confirmButton.addClickListener(event -> {
            EntryPojo entryPojo = new EntryPojo();
            try {
                entryPojoBinder.writeBean(entryPojo);
            } catch (ValidationException e) {
                e.printStackTrace();
            }
            //TODO Need to add saving to database
            dialog.close();
        });
        return confirmButton;
    }

    private HorizontalLayout createButtonLayout() {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        return buttonLayout;
    }

    private ComboBox<AccountCharacter> createAccountCharacterPojoComboBox(List<AccountCharacter> accountCharacterPojoList) {
        ComboBox<AccountCharacter> accountCharacters = new ComboBox<>();
        accountCharacters.setItemLabelGenerator(AccountCharacter::getCharacterName);
        accountCharacters.setItems(accountCharacterPojoList);
        accountCharacters.setLabel("Account Characters");
        accountCharacters.setValue(accountCharacterPojoList.get(0));
        return accountCharacters;
    }

    private FormLayout createEntryFormLayout() {
        FormLayout entryFormLayout = new FormLayout();
        entryFormLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP));
        return entryFormLayout;
    }

    private VerticalLayout createMainDialogLayout() {
        VerticalLayout mainDialogBoxLayout = new VerticalLayout();
        mainDialogBoxLayout.setMaxWidth("400px");
        return mainDialogBoxLayout;
    }

    private void configureDialog() {
        this.setCloseOnEsc(true);
        this.setDraggable(true);
        this.setCloseOnOutsideClick(false);
    }

}
