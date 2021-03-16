package com.trix.wowgarrisontracker.frontEnd;

import com.trix.wowgarrisontracker.pojos.AccountCharacterPojo;
import com.trix.wowgarrisontracker.services.interfaces.AccountCharacterService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
@UIScope
public class CharacterFormDialog extends Dialog {

    @Autowired
    private AccountCharacterService characterService;
    private Binder<AccountCharacterPojo> binder = new Binder<>();
    private AccountCharacterPojo accountCharacterPojo = new AccountCharacterPojo();
    private Long id;
    private Grid<AccountCharacterPojo> grid;

    public CharacterFormDialog() {
    }

    @PostConstruct
    private void init() {
        configureDialog();
        binder.readBean(accountCharacterPojo);
        accountCharacterPojo.setAccountId(id);

        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.getStyle().set("background","#252422");

        FormLayout formLayout = new FormLayout();
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP));
        mainLayout.add(formLayout);

        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setWidthFull();
        buttonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        mainLayout.add(buttonLayout);
        add(mainLayout);


        TextField characterNameField = createCharacterNameTextField();
        formLayout.add(characterNameField);

        Button createButton = new Button("Create");
        createButton.addClickListener(event -> {
            try {
                binder.writeBean(accountCharacterPojo);
                this.close();
                accountCharacterPojo.setAccountId(id);
                characterService.save(accountCharacterPojo);
                accountCharacterPojo = new AccountCharacterPojo();
                accountCharacterPojo.setAccountId(id);
                binder.readBean(accountCharacterPojo);
                List<AccountCharacterPojo> listOfAccountCharactersConvertedToPojo = characterService.getListOfAccountCharactersConvertedToPojo(id);
                grid.setItems(listOfAccountCharactersConvertedToPojo);

            } catch (ValidationException e) {
                e.getMessage();
            }

        });

        Button cancelButton = new Button("Cancel");
        cancelButton.addClickListener(event -> this.close());

        buttonLayout.add(createButton, cancelButton);

    }

    private TextField createCharacterNameTextField() {
        TextField characterNameField = new TextField("Character Name");
        characterNameField.setPlaceholder("Hokmurzok");
        binder.forField(characterNameField)
                .withValidator(validate ->
                        !characterService.isNameTaken(id, characterNameField.getValue()), "Name is taken")
                .withValidator(new StringLengthValidator("Character name must be between 0 and 100 characters", 0, 100))
                .bind(AccountCharacterPojo::getCharacterName, AccountCharacterPojo::setCharacterName);
        return characterNameField;
    }

    private void configureDialog() {
        this.setCloseOnEsc(true);
        this.setDraggable(true);
        this.setCloseOnOutsideClick(false);

    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setGrid(Grid<AccountCharacterPojo> grid) {
        this.grid = grid;
    }
}
