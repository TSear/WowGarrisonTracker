package com.trix.wowgarrisontracker.frontEnd;

import com.trix.wowgarrisontracker.model.AccountCharacter;
import com.trix.wowgarrisontracker.pojos.EntryPojo;
import com.trix.wowgarrisontracker.services.interfaces.AccountCharacterService;
import com.trix.wowgarrisontracker.services.interfaces.EntryService;
import com.trix.wowgarrisontracker.utils.GeneralUtils;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.vaadin.klaudeta.PaginatedGrid;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Objects;


@Component
@UIScope
public class EntryFormDialog extends Dialog {

    private final static String REQUIRED = "Please fill this field";
    private final static String NUMBER_RANGE_ERROR_MESSAGE = "You must pass number between 0-10000";
    @Autowired
    private AccountCharacterService accountCharacterService;
    @Autowired
    private EntryService entryService;
    private Binder<EntryPojo> entryPojoBinder = new Binder<>();
    private EntryPojo entryPojo = new EntryPojo();
    private PaginatedGrid<EntryPojo> layout;
    private Long id;
    private TrackLayout parent;
    private GeneralUtils utils;


    public EntryFormDialog() {
        this.utils = new GeneralUtils();
        this.entryService = entryService;
        this.accountCharacterService = accountCharacterService;
    }

    @PostConstruct
    private void generateDialogBox() {
        this.configureDialog();


        entryPojoBinder.readBean(entryPojo);


        Long id =  utils.getCurrentlyLoggedUserId();

        VerticalLayout mainDialogBoxLayout = createMainDialogLayout();

        FormLayout entryFormLayout = createEntryFormLayout();

        List<AccountCharacter> accountCharacterList = accountCharacterService.findAllByAccountId(id);

        ComboBox<AccountCharacter> accountCharactersComboBox = createAccountCharacterPojoComboBox();

        IntegerField garrisonResourcesIntegerField = createGarrisonResourcesIntegerField();

        IntegerField warPaintIntegerField = createWarPaintIntegerField();

        entryFormLayout.add(accountCharactersComboBox);
        entryFormLayout.add(garrisonResourcesIntegerField);
        entryFormLayout.add(warPaintIntegerField);

        mainDialogBoxLayout.add(entryFormLayout);

        HorizontalLayout buttonLayout = createButtonLayout();

        Button confirmButton = createCreateEntryButton(this, "Create Entry");
        buttonLayout.setFlexGrow(1, confirmButton);

        Button cancelButton = createCancelButton(this, "Cancel");

        buttonLayout.add(confirmButton, cancelButton);
        mainDialogBoxLayout.add(buttonLayout);

        this.add(mainDialogBoxLayout);
        mainDialogBoxLayout.setFlexGrow(1, buttonLayout);

        this.addOpenedChangeListener(event -> {
            if(event.isOpened()){
                System.out.println("called");
                accountCharactersComboBox.setItems(accountCharacterService.findAllByAccountId(id));
            }
        });

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
                .withValidator(value -> value != null, REQUIRED)
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
        cancelButton.addClassName("secondary-button");
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
                entryService.save(entryPojo);
                dialog.close();
                entryPojo.clean();
                entryPojoBinder.readBean(entryPojo);
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

    private ComboBox<AccountCharacter> createAccountCharacterPojoComboBox() {
        ComboBox<AccountCharacter> accountCharacters = new ComboBox<>();

        accountCharacters.setItemLabelGenerator(AccountCharacter::getCharacterName);
        accountCharacters.setLabel("Account Character");
        accountCharacters.setAllowCustomValue(false);
        accountCharacters.addAttachListener(event -> {
            accountCharacters.setRequired(true);
            accountCharacters.setRequiredIndicatorVisible(true);
        });
        accountCharacters.setPlaceholder("Chose Character");
        entryPojoBinder.forField(accountCharacters)
                .withValidator(Objects::nonNull, REQUIRED)
                .bind(EntryPojo::getAccountCharacter,
                        (entryPojo1, accountCharacter) -> entryPojo1.setAccountCharacterId(accountCharacter.getId()));

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

    private void configureDialog() {
        this.setCloseOnEsc(true);
        this.setDraggable(true);
        this.setCloseOnOutsideClick(false);

    }

    public void setView(TrackLayout tmp) {
        this.parent = tmp;
    }

    public void setGrid(PaginatedGrid<EntryPojo> layout) {
        this.layout = layout;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
