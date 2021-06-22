package com.trix.wowgarrisontracker.frontEnd;

import com.trix.wowgarrisontracker.frontEnd.fragments.AddButton;
import com.trix.wowgarrisontracker.frontEnd.fragments.MainLayout;
import com.trix.wowgarrisontracker.frontEnd.interfaces.Refreshable;
import com.trix.wowgarrisontracker.pojos.AccountCharacterPojo;
import com.trix.wowgarrisontracker.services.interfaces.AccountCharacterService;
import com.trix.wowgarrisontracker.utils.GeneralUtils;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import com.vaadin.flow.spring.annotation.VaadinSessionScope;
import org.springframework.context.annotation.Profile;
import org.vaadin.klaudeta.PaginatedGrid;

import javax.annotation.PostConstruct;
import java.util.List;

@Profile("vaadin")
@UIScope
@Route(value = "characters", layout = MainLayout.class)
public class CharactersLayout extends VerticalLayout implements Refreshable {

    private static final int PAGE_SIZE = 18;
    private final AccountCharacterService accountCharacterService;
    private final Statistics statistics;
    private final Dialog confirmDelete;

    private CharacterFormDialog dialog;
    private final Long id;
    private PaginatedGrid<AccountCharacterPojo> dataGridLayout;
    private DataProvider<AccountCharacterPojo, Void> dataProvider;


    public CharactersLayout(AccountCharacterService accountCharacterService, Statistics statistics) {
        this.accountCharacterService = accountCharacterService;
        this.statistics = statistics;
        this.id = GeneralUtils.getCurrentlyLoggedUserId();
        confirmDelete = new Dialog();
    }

    @PostConstruct
    private void init() {

        configureDataProvider();
        configureFrame();

        dialog = new CharacterFormDialog(accountCharacterService, id, this);

        dataGridLayout = createGridLayout();

        HorizontalLayout gridButtonLayout = new HorizontalLayout();
        gridButtonLayout.setWidthFull();

        AddButton addNewCharacterButton = new AddButton("Create New Character", dialog);
        gridButtonLayout.setFlexGrow(1, addNewCharacterButton);

        Button deleteCharacterButton = createDeleteButton();

        createDeleteConfirmDialog();


        gridButtonLayout.add(addNewCharacterButton);
        gridButtonLayout.add(deleteCharacterButton);

        add(dataGridLayout);
        add(gridButtonLayout);
        add(confirmDelete);
    }

    private void createDeleteConfirmDialog() {
        HorizontalLayout dialogButtonLayout = new HorizontalLayout();
        Button dialogConfigButton = new Button("Confirm");
        Button dialogCancelButton = createDialogCancelButton();

        dialogButtonLayout.add(dialogConfigButton, dialogCancelButton);
        confirmDelete.add(new Text("Are you sure you want to delete character"), dialogButtonLayout);
    }

    private Button createDialogCancelButton() {
        Button dialogCancelButton = new Button("Cancel");

        dialogCancelButton.addClassName("secondary-button");
        dialogCancelButton.addClickListener(event -> confirmDelete.close());
        return dialogCancelButton;
    }

    private void configureFrame() {
        setHeightFull();
        setClassName("content-background");
    }

    private void configureDataProvider() {

        dataProvider = DataProvider.fromCallbacks(
                query -> {
                    long offset = dataGridLayout == null || dataGridLayout.getPage() < 1 ? 0L : dataGridLayout.getPage() - 1;
                    List<AccountCharacterPojo> data = accountCharacterService.getAllAccountCharactersPagedPojo(id, (int) offset, PAGE_SIZE);
                    return data.stream();
                }, query -> accountCharacterService.countAllAccountCharactersByAccountId(id));
    }

    private PaginatedGrid<AccountCharacterPojo> createGridLayout() {
        PaginatedGrid<AccountCharacterPojo> tmp = new PaginatedGrid<>();
        setFlexGrow(1, tmp);
        tmp.addColumn(AccountCharacterPojo::getCharacterName).setHeader("Character Name");
        tmp.addColumn(AccountCharacterPojo::getGarrisonResources).setHeader("Garrison Resources");
        tmp.addColumn(AccountCharacterPojo::getWarPaint).setHeader("War paint");
        tmp.setWidth("100%");
        tmp.setDataProvider(dataProvider);
        tmp.setPageSize(PAGE_SIZE);
        return tmp;
    }

    private Button createDeleteButton() {
        Button deleteEntryButton = new Button();
        deleteEntryButton.addClassName("secondary-button");
        deleteEntryButton.setIcon(VaadinIcon.TRASH.create());
        deleteEntryButton.setIconAfterText(true);
        deleteEntryButton.addClickListener(event -> {
            dataGridLayout.getSelectedItems().forEach(accountCharacterPojo -> accountCharacterService.delete(accountCharacterPojo.getId()));
            dataGridLayout.setItems(accountCharacterService.getListOfAccountCharactersConvertedToPojo(id));
            confirmDelete.close();
            statistics.refresh();
        });
        return deleteEntryButton;
    }

    @Override
    public void refresh() {
        dataProvider.refreshAll();
    }
}
