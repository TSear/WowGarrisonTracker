package com.trix.wowgarrisontracker.frontEnd.views.resources;

import com.trix.wowgarrisontracker.frontEnd.LayoutVariables;
import com.trix.wowgarrisontracker.frontEnd.views.statistics.StatisticsView;
import com.trix.wowgarrisontracker.frontEnd.components.AddButton;
import com.trix.wowgarrisontracker.frontEnd.fragments.MainLayout;
import com.trix.wowgarrisontracker.frontEnd.interfaces.Refreshable;
import com.trix.wowgarrisontracker.model.Entry;
import com.trix.wowgarrisontracker.services.interfaces.AccountCharacterService;
import com.trix.wowgarrisontracker.services.interfaces.EntryService;
import com.trix.wowgarrisontracker.utils.GeneralUtils;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.PageRequest;
import org.vaadin.klaudeta.PaginatedGrid;

import javax.annotation.PostConstruct;
import javax.annotation.security.PermitAll;
import java.util.Collection;
import java.util.List;

@Profile("vaadin")
@UIScope
@CssImport(value = "/css/globalVariables.css")
@CssImport(value = "/css/global.css")
@CssImport(value = "/css/textField.css", themeFor = "vaadin-text-field")
@CssImport(value = "/css/textField.css", themeFor = "vaadin-password-field")
@CssImport(value = "/css/grid.css", themeFor = "vaadin-grid")
@CssImport(value = "/css/login.css", themeFor = "vaadin-login-form-wrapper")
@CssImport(value = "/css/dialogBox.css", themeFor = "vaadin-dialog-overlay")
@CssImport(value = "/css/statistics.css")
@CssImport(value = "/css/contact.css")
@RouteAlias(value = "", layout = MainLayout.class)
@PermitAll
@PageTitle("Resources")
@Route(value = "track", layout = MainLayout.class)
public class ResourcesView extends VerticalLayout implements Refreshable {

    private final static int PAGE_SIZE = 17;

    private final EntryService entryService;

    private final AccountCharacterService accountCharacterService;

    private final StatisticsView statistics;

    private NewEntryDialogForm entryFormDialog;
    private Long id;
    private PaginatedGrid<Entry> gridLayout;
    private DataProvider<Entry, Void> dataProvider;

    public ResourcesView(EntryService entryService, AccountCharacterService accountCharacterService, StatisticsView statistics) {
        this.entryService = entryService;
        this.accountCharacterService = accountCharacterService;
        this.statistics = statistics;
    }


    @PostConstruct
    private void init() {

        id = GeneralUtils.getCurrentlyLoggedUserId();

        configureDataProvider();
        configureMainLayout();
        entryFormDialog = new NewEntryDialogForm(accountCharacterService, id, this, statistics, entryService);


        gridLayout = createGridLayout();

        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setWidthFull();

        Button addButton = createAddEntryButton(entryFormDialog);
        buttonLayout.setFlexGrow(1, addButton);

        Button deleteEntryButton = createDeleteButton(gridLayout);

        buttonLayout.add(addButton);
        buttonLayout.add(deleteEntryButton);

        createContextMenu(gridLayout);

        add(gridLayout);
        add(buttonLayout);
        add(entryFormDialog);
    }

    private Button createAddEntryButton(NewEntryDialogForm entryFormDialog) {
        Button button = new AddButton("Add New Entry", entryFormDialog);
        button.setClassName("contrast-button");
        setWidthFull();
        return button;
    }

    private void createContextMenu(PaginatedGrid<Entry> gridLayout) {
        GridContextMenu<Entry> contextMenu = new GridContextMenu<>();
        contextMenu.setTarget(gridLayout);
        contextMenu.setClassName(LayoutVariables.CONTEXT_MENU);

        contextMenu.addItem("Create new entry",entryGridContextMenuItemClickEvent -> entryFormDialog.open());

        contextMenu.addItem("Delete entry", buttonClickEvent -> {
            int selectedSize = gridLayout.getSelectedItems().size();
            if (selectedSize == 0) {
                Notification notification = new Notification("You need to select entry", 5000);
                add(notification);
                notification.open();
            } else {
                removeEntries(gridLayout.getSelectedItems());
                contextMenu.close();
            }
        });


    }

    private void removeEntries(Collection<Entry> entries) {
        entries.forEach(entry -> entryService.delete(entry.getId()));
        statistics.refresh();
        dataProvider.refreshAll();
    }

    private void configureDataProvider() {

        dataProvider = DataProvider.fromCallbacks(
                query -> {
                    int offset = gridLayout == null || gridLayout.getPage() < 1 ? 0 : gridLayout.getPage() - 1;
                    List<Entry> data = entryService.getAllAccountEntriesPaged(id, PageRequest.of(offset, PAGE_SIZE));
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
                removeEntries(grid.getSelectedItems());
            }

        });
        return deleteEntryButton;
    }

    private void configureMainLayout() {
        this.getStyle().set("padding-bottom","0");
        this.setClassName(LayoutVariables.PRIMARY_BACKGROUND);

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
        gridLayout.addColumn(entry -> entry.getAccountCharacter().getCharacterName()).setHeader("Character Name");
        gridLayout.addColumn(Entry::getGarrisonResources).setHeader("Garrison Resources");
        gridLayout.addColumn(Entry::getWarPaint).setHeader("War Paint");


        return gridLayout;
    }


    @Override
    public void refresh() {
        this.dataProvider.refreshAll();
    }
}
