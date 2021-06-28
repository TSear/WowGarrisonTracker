package com.trix.wowgarrisontracker.frontEnd;

import com.trix.wowgarrisontracker.frontEnd.interfaces.Refreshable;
import com.trix.wowgarrisontracker.model.AccountCharacter;
import com.trix.wowgarrisontracker.model.Entry;
import com.trix.wowgarrisontracker.services.interfaces.AccountCharacterService;
import com.trix.wowgarrisontracker.services.interfaces.EntryService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.validator.IntegerRangeValidator;
import com.vaadin.flow.spring.annotation.UIScope;

import java.util.Objects;


@UIScope
public class EntryFormDialog extends Dialog {

    private final static String REQUIRED = "Please fill this field";
    private final static String NUMBER_RANGE_ERROR_MESSAGE = "You must pass number between 0-10000";
    private final static int MAX_TEXT_FIELD_VALUE = 10000;
    private final static int START_VALUE = 0;
    private final static int VALUE_STEP = 1;
    private final static int MIN_TEXT_FIELD_VALUE = 0;

    private final AccountCharacterService accountCharacterService;
    private final EntryService entryService;
    private final Binder<Entry> entryPojoBinder = new Binder<>();
    private Entry entry = new Entry();
    private final Statistics statistics;
    private final Refreshable parent;

    private final Long id;


    public EntryFormDialog(AccountCharacterService accountCharacterService,
                           Long id,
                           Refreshable parent,
                           Statistics statistics,
                           EntryService entryService) {
        this.accountCharacterService = accountCharacterService;
        this.id = id;
        this.parent = parent;
        this.statistics = statistics;
        this.entryService = entryService;
        generateDialogBox();
    }

    private void generateDialogBox() {

        this.configureDialogFrame();


        VerticalLayout mainDialogBoxLayout = createMainDialogLayout();

        FormLayout entryFormLayout = createEntryFormLayout();

        ComboBox<AccountCharacter> accountCharactersComboBox = createAccountCharacterPojoComboBox();

        IntegerField garrisonResourcesIntegerField = createGarrisonResourcesIntegerField();

        IntegerField warPaintIntegerField = createWarPaintIntegerField();

        HorizontalLayout buttonLayout = createButtonLayout();

        Button confirmButton = createAddEntryButton();
        buttonLayout.setFlexGrow(1, confirmButton);

        Button cancelButton = createCancelButton();

        mainDialogBoxLayout.setFlexGrow(1, buttonLayout);

        //TODO need to replace this with something more appropriate
        this.addOpenedChangeListener(event -> {
            if (event.isOpened()) {
                accountCharactersComboBox.setItems(accountCharacterService.findAllByAccountId(id));
            }
        });

        entryPojoBinder.readBean(entry);

        entryFormLayout.add(accountCharactersComboBox);
        entryFormLayout.add(garrisonResourcesIntegerField);
        entryFormLayout.add(warPaintIntegerField);

        buttonLayout.add(confirmButton, cancelButton);

        mainDialogBoxLayout.add(entryFormLayout);
        mainDialogBoxLayout.add(buttonLayout);

        add(mainDialogBoxLayout);

    }

    private IntegerField createWarPaintIntegerField() {

        IntegerField warPaint = createBaseIntegerField("War Paint");

        entryPojoBinder.forField(warPaint)
                .withValidator(new IntegerRangeValidator(NUMBER_RANGE_ERROR_MESSAGE,
                        MIN_TEXT_FIELD_VALUE,
                        MAX_TEXT_FIELD_VALUE))
                .withValidator(Objects::nonNull, REQUIRED)
                .bind(Entry::getWarPaint, Entry::setWarPaint);
        return warPaint;
    }

    private IntegerField createGarrisonResourcesIntegerField() {

        IntegerField garrisonResources = createBaseIntegerField("Garrison Resources");

        entryPojoBinder.forField(garrisonResources)
                .withValidator(new IntegerRangeValidator(NUMBER_RANGE_ERROR_MESSAGE,
                        MIN_TEXT_FIELD_VALUE,
                        MAX_TEXT_FIELD_VALUE))
                .withValidator(Objects::nonNull, REQUIRED)
                .bind(Entry::getGarrisonResources, Entry::setGarrisonResources);

        return garrisonResources;
    }

    private IntegerField createBaseIntegerField(String labelText) {

        IntegerField integerField = new IntegerField(labelText);
        integerField.setValue(START_VALUE);
        integerField.setMin(MIN_TEXT_FIELD_VALUE);
        integerField.setMax(MAX_TEXT_FIELD_VALUE);
        integerField.setStep(VALUE_STEP);
        integerField.setHasControls(true);

        return integerField;
    }

    private Button createCancelButton() {
        Button cancelButton = new Button("Cancel");
        cancelButton.addClassName("secondary-button");
        cancelButton.addClickListener(event -> this.close());
        return cancelButton;
    }

    private Button createAddEntryButton() {
        Button confirmButton = new Button("Create Entry");
        confirmButton.addClickListener(event -> addEntryEventHandler());
        return confirmButton;
    }

    private void addEntryEventHandler() {
        try {
            entryPojoBinder.writeBean(entry);
            createEntryAndCleanDialog();
        } catch (ValidationException e) {
            //TODO this is temporary. Need to replace with something more appropriate
            System.out.println("Validation error");
        }
    }

    private void createEntryAndCleanDialog() {

        entryService.save(entry);
        entry = new Entry();
        entryPojoBinder.readBean(entry);

        refreshOtherPages();
        this.close();

    }

    private void refreshOtherPages() {
        parent.refresh();
        statistics.refresh();
    }


    private HorizontalLayout createButtonLayout() {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        return buttonLayout;
    }

    private ComboBox<AccountCharacter> createAccountCharacterPojoComboBox() {

        ComboBox<AccountCharacter> accountCharacters = new ComboBox<>();

        accountCharacters.setItemLabelGenerator(AccountCharacter::getCharacterName);
        accountCharacters.setLabel("Character Name");
        accountCharacters.setAllowCustomValue(false);
        accountCharacters.setRequired(true);
        accountCharacters.setRequiredIndicatorVisible(true);
        accountCharacters.setPlaceholder("Chose Character");

        entryPojoBinder.forField(accountCharacters)
                .withValidator(Objects::nonNull, REQUIRED)
                .bind(Entry::getAccountCharacter,
                        Entry::setAccountCharacter);

        return accountCharacters;
    }


    private FormLayout createEntryFormLayout() {
        FormLayout entryFormLayout = new FormLayout();
        entryFormLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP));
        entryFormLayout.setWidthFull();
        return entryFormLayout;
    }

    private VerticalLayout createMainDialogLayout() {
        VerticalLayout mainDialogBoxLayout = new VerticalLayout();
        mainDialogBoxLayout.setClassName("dialog-layout");
        mainDialogBoxLayout.setSizeFull();
        mainDialogBoxLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        return mainDialogBoxLayout;
    }

    private void configureDialogFrame() {
        this.setCloseOnEsc(true);
        this.setDraggable(true);
        this.setCloseOnOutsideClick(true);

    }

}
