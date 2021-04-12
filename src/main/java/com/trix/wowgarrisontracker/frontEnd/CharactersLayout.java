package com.trix.wowgarrisontracker.frontEnd;

import com.trix.wowgarrisontracker.frontEnd.fragments.AddButton;
import com.trix.wowgarrisontracker.frontEnd.fragments.MainLayout;
import com.trix.wowgarrisontracker.pojos.AccountCharacterPojo;
import com.trix.wowgarrisontracker.services.interfaces.AccountCharacterService;
import com.trix.wowgarrisontracker.utils.GeneralUtils;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;

import javax.annotation.PostConstruct;

@Profile("vaadin")
@UIScope
@Route(value = "characters", layout = MainLayout.class)
public class CharactersLayout extends VerticalLayout {

    @Autowired
    private CharacterFormDialog dialog;
    @Autowired
    private AccountCharacterService accountCharacterService;
    @Autowired
    private GeneralUtils utils;
    private Long id;
    private Dialog confirmDelete;

    public CharactersLayout() {
        confirmDelete = new Dialog();
    }

    @PostConstruct
    private void init() {
        setHeightFull();
        setClassName("content-background");

        id = utils.getCurrentlyLoggedUserId();

        dialog.setId(id);

        Grid<AccountCharacterPojo> accountCharacterPojoGrid = createGridLayout();
        setFlexGrow(1, accountCharacterPojoGrid);

        this.add(accountCharacterPojoGrid);

        dialog.setGrid(accountCharacterPojoGrid);

        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setWidthFull();

        new AddButton(buttonLayout, "Create New Character", dialog);

        Button deleteCharacterButton = createDeleteButton(accountCharacterPojoGrid);
        deleteCharacterButton.addClassName("secondary-button");
        buttonLayout.add(deleteCharacterButton);

        Text dialogMessage = new Text("Are you sure you want to delete character");

        HorizontalLayout dialogButtonLayout = new HorizontalLayout();
        Button confirmButton = new Button("Confirm");
        Button cancelButton = new Button("Cancel");

        cancelButton.addClassName("secondary-button");

        confirmButton.addClickListener(event -> {
            accountCharacterPojoGrid.getSelectedItems().forEach(accountCharacterPojo -> accountCharacterService.delete(accountCharacterPojo.getId()));
            accountCharacterPojoGrid.setItems(accountCharacterService.getListOfAccountCharactersConvertedToPojo(id));
            confirmDelete.close();
        });

        cancelButton.addClickListener(event -> confirmDelete.close());

        dialogButtonLayout.add(confirmButton, cancelButton);
        confirmDelete.add(dialogMessage, dialogButtonLayout);

        add(buttonLayout);
    }

    private Grid<AccountCharacterPojo> createGridLayout() {
        Grid<AccountCharacterPojo> tmp = new Grid<>();
        tmp.setItems(accountCharacterService.getListOfAccountCharactersConvertedToPojo(id));
        tmp.addColumn(AccountCharacterPojo::getCharacterName).setHeader("Character Name");
        tmp.addColumn(AccountCharacterPojo::getGarrisonResources).setHeader("Garrison Resources");
        tmp.addColumn(AccountCharacterPojo::getWarPaint).setHeader("War paint");
        tmp.setWidth("100%");
        return tmp;
    }

    private Button createDeleteButton(Grid<AccountCharacterPojo> grid) {
        Button deleteEntryButton = new Button();
        deleteEntryButton.setIcon(VaadinIcon.TRASH.create());
        deleteEntryButton.setIconAfterText(true);
        deleteEntryButton.addClickListener(event -> {
            if (grid.getSelectedItems().size() > 0) {
                confirmDelete.open();
            }

        });
        return deleteEntryButton;
    }

}
