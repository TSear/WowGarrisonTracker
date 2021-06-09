package com.trix.wowgarrisontracker.frontEnd;

import com.trix.wowgarrisontracker.frontEnd.fragments.AddButton;
import com.trix.wowgarrisontracker.frontEnd.fragments.MainLayout;
import com.trix.wowgarrisontracker.frontEnd.interfaces.Refreshable;
import com.trix.wowgarrisontracker.pojos.Entry;
import com.trix.wowgarrisontracker.services.interfaces.AccountCharacterService;
import com.trix.wowgarrisontracker.services.interfaces.EntryService;
import com.trix.wowgarrisontracker.utils.GeneralUtils;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.context.annotation.Profile;
import org.vaadin.klaudeta.PaginatedGrid;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

@Profile("vaadin")
@UIScope
@CssImport(value = "./globalVariables.css")
@CssImport(value = "./css/global.css")
@CssImport(value = "./textField.css", themeFor = "vaadin-text-field")
@CssImport(value = "./textField.css", themeFor = "vaadin-password-field")
@CssImport(value = "./grid.css", themeFor = "vaadin-grid")
@CssImport(value = "./login.css", themeFor = "vaadin-login-form-wrapper")
@CssImport(value = "./dialogBox.css", themeFor = "vaadin-dialog-overlay")
@CssImport(value = "./statistics.css")
@CssImport(value = "./contact.css")
@RouteAlias(value = "", layout = MainLayout.class)
@Route(value = "track", layout = MainLayout.class)
public class TrackLayout extends VerticalLayout implements Refreshable {

    private final static int PAGE_SIZE = 17;

    private final EntryService entryService;

    private final AccountCharacterService accountCharacterService;

    private final Statistics statistics;

    private EntryFormDialog entryFormDialog;
    private Long id;
    private PaginatedGrid<Entry> gridLayout;
    private DataProvider<Entry, Void> dataProvider;

    public TrackLayout(EntryService entryService, AccountCharacterService accountCharacterService, Statistics statistics) {
        this.entryService = entryService;
        this.accountCharacterService = accountCharacterService;
        this.statistics = statistics;
    }


    @PostConstruct
    private void init() {

        id = GeneralUtils.getCurrentlyLoggedUserId();

        configureDataProvider();
        configureMainLayout();
        entryFormDialog = new EntryFormDialog(accountCharacterService, id, this, statistics);


        gridLayout = createGridLayout();

        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setWidthFull();

        Button addButton = new AddButton("Add New Entry", entryFormDialog);
        buttonLayout.setFlexGrow(1, addButton);

        Button deleteEntryButton = createDeleteButton(gridLayout);

        buttonLayout.add(addButton);
        buttonLayout.add(deleteEntryButton);

        add(gridLayout);
        add(buttonLayout);
        add(entryFormDialog);
    }

    private void configureDataProvider() {

        dataProvider = DataProvider.fromCallbacks(
                query -> {
                    long offset = gridLayout == null || gridLayout.getPage() < 1 ? 0L : gridLayout.getPage() - 1;
                    List<Entry> data = entryService.getAllAccountEntriesPagedPojo(id, offset, (long) PAGE_SIZE);
                    return data.stream();
                }, query -> entryService.getAmountOfEntries(id));
    }

    private Button createDeleteButton(Grid<Entry> grid) {
        Button deleteEntryButton = new Button();
        deleteEntryButton.addClassName("secondary-button");
        deleteEntryButton.setIcon(VaadinIcon.TRASH.create());
        deleteEntryButton.setIconAfterText(true);
        deleteEntryButton.addClickListener(event -> {
            if (grid.getSelectedItems().size() > 0) {
                grid.getSelectedItems().forEach(accountCharacterService::removeEntryFromAccountCharacter);
                statistics.update();
                dataProvider.refreshAll();
            }

        });
        return deleteEntryButton;
    }

    private void configureMainLayout() {
        this.setHeightFull();
        this.setPadding(true);
        this.setClassName("content-background");
    }

    private PaginatedGrid<Entry> createGridLayout() {
        PaginatedGrid<Entry> gridLayout = new PaginatedGrid<>();

        gridLayout.setDataProvider(dataProvider);
        gridLayout.setClassName("track-grid");
        gridLayout.setPaginatorSize(2);
        setFlexGrow(1, gridLayout);
        gridLayout.setPageSize(PAGE_SIZE);
        gridLayout.setSelectionMode(Grid.SelectionMode.MULTI);


        gridLayout.addColumn(Entry::getEntryDate).setHeader("Entry Date");
        gridLayout.addColumn(Entry::getCharacterName).setHeader("Character Name");
        gridLayout.addColumn(Entry::getGarrisonResources).setHeader("Garrison Resources");
        gridLayout.addColumn(Entry::getWarPaint).setHeader("War Paint");


        return gridLayout;
    }


    @Override
    public void refresh() {
        this.dataProvider.refreshAll();
    }
}
