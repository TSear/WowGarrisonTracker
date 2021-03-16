package com.trix.wowgarrisontracker.frontEnd;

import com.trix.wowgarrisontracker.frontEnd.fragments.TrackLayout;
import com.trix.wowgarrisontracker.model.AccountCharacter;
import com.trix.wowgarrisontracker.pojos.EntryPojo;
import com.trix.wowgarrisontracker.services.interfaces.AccountCharacterService;
import com.trix.wowgarrisontracker.services.interfaces.EntryService;
import com.trix.wowgarrisontracker.utils.GeneralUtils;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.validator.IntegerRangeValidator;
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

    @Autowired
    private GeneralUtils utils;
    @Autowired
    private AccountCharacterService accountCharacterService;
    @Autowired
    private EntryService entryService;

    private Binder<EntryPojo> entryPojoBinder = new Binder<>();
    private EntryPojo entryPojo = new EntryPojo();
    private final static String REQUIRED = "Please fill this field";
    private final static String NUMBER_RANGE_ERROR_MESSAGE = "You must pass number between 0-10000";
    private Grid<EntryPojo> layout;
    private Long id;


    public EntryFormDialog() {

        this.entryService = entryService;
        this.utils = utils;
        this.accountCharacterService = accountCharacterService;
    }

    @PostConstruct
    private void generateDialogBox() {
        this.configureDialog();

        entryPojoBinder.readBean(entryPojo);

        Long id = utils.getId(VaadinService.getCurrentRequest().getCookies());

        VerticalLayout mainDialogBoxLayout = createMainDialogLayout();

        FormLayout entryFormLayout = createEntryFormLayout();

        List<AccountCharacter> accountCharacterList = accountCharacterService.findAllByAccountId(id);

        ComboBox<AccountCharacter> accountCharactersComboBox = createAccountCharacterPojoComboBox(accountCharacterList);

        IntegerField garrisonResourcesIntegerField = createGarrisonResourcesIntegerField();

        IntegerField warPaintIntegerField = createWarPaintIntegerField();

        entryFormLayout.add(accountCharactersComboBox);
        entryFormLayout.add(garrisonResourcesIntegerField);
        entryFormLayout.add(warPaintIntegerField);

        mainDialogBoxLayout.add(entryFormLayout);

        HorizontalLayout buttonLayout = createButtonLayout();

        Button confirmButton = createCreateEntryButton(this, "Create Entry");

        Button cancelButton = createCancelButton(this, "Cancel");

        buttonLayout.add(confirmButton, cancelButton);
        mainDialogBoxLayout.add(buttonLayout);

        this.add(mainDialogBoxLayout);
        mainDialogBoxLayout.setFlexGrow(1, buttonLayout);

    }

    private IntegerField createWarPaintIntegerField() {
        IntegerField warPaint = new IntegerField("War Paint");
        warPaint.setValue(0);
        warPaint.setMax(5000);
        warPaint.setMin(0);
        warPaint.setStep(1);
        warPaint.setHasControls(true);
        entryPojoBinder.forField(warPaint)
                .withValidator(new IntegerRangeValidator(NUMBER_RANGE_ERROR_MESSAGE, 0, 10000))
                .withValidator(value -> value!= null, REQUIRED)
                .bind(EntryPojo::getWarPaint, EntryPojo::setWarPaint);
        return warPaint;
    }

    private IntegerField createGarrisonResourcesIntegerField() {
        IntegerField garrisonResources = new IntegerField("Garrison Resources");
        garrisonResources.setValue(0);
        garrisonResources.setMin(0);
        garrisonResources.setMax(10000);
        garrisonResources.setStep(1);
        garrisonResources.setHasControls(true);

        entryPojoBinder.forField(garrisonResources)
                .withValidator(new IntegerRangeValidator(NUMBER_RANGE_ERROR_MESSAGE,
                        0,
                        10000))
                .withValidator(integer -> integer != null, REQUIRED)
                .bind(EntryPojo::getGarrisonResources, EntryPojo::setGarrisonResources);

        return garrisonResources;
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
            try {
                entryPojoBinder.writeBean(entryPojo);
                dialog.close();
                entryService.save(entryPojo);
                entryPojo.clean();
                entryPojoBinder.readBean(entryPojo);
                //TODO THIS IS TEMPORARY IMPLEMENTATION. NEED TO CHANGE ASAP
                layout.setItems(entryService.accountEntriesConvertedToPojoList(id));

            } catch (ValidationException e) {
                System.out.println(e.getMessage());
            }
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
        accountCharacters.setAllowCustomValue(false);
        accountCharacters.addAttachListener(event -> {
            accountCharacters.setRequired(true);
            accountCharacters.setRequiredIndicatorVisible(true);
        });
        accountCharacters.setPlaceholder("Chose Characters");
        entryPojoBinder.forField(accountCharacters)
                .withValidator(selected -> selected != null , REQUIRED)
                .bind(EntryPojo::getAccountCharacter, EntryPojo::setAccountCharacter);

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
        mainDialogBoxLayout.setClassName("dialog-layout");
        mainDialogBoxLayout.setSizeFull();
        mainDialogBoxLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        return mainDialogBoxLayout;
    }

    private void configureDialog() {
        this.setCloseOnEsc(true);
        this.setDraggable(true);
        this.setCloseOnOutsideClick(false);

    }

    public void setGrid(Grid<EntryPojo> layout) {
        this.layout = layout;
    }
    public void setId(Long id){
        this.id = id;
    }
}
