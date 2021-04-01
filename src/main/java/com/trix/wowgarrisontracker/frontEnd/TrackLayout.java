package com.trix.wowgarrisontracker.frontEnd;

import com.trix.wowgarrisontracker.frontEnd.fragments.AddButton;
import com.trix.wowgarrisontracker.frontEnd.fragments.MainLayout;
import com.trix.wowgarrisontracker.pojos.EntryPojo;
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
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.vaadin.klaudeta.PaginatedGrid;

import javax.annotation.PostConstruct;
import java.util.List;

@Profile("vaadin")
@CssImport(value = "./grid.css", themeFor = "vaadin-grid")
@CssImport(value = "./checkBox.css", themeFor = "vaadin-checkbox")
@UIScope
@Route(value = "track", layout = MainLayout.class)
public class TrackLayout extends VerticalLayout {


    @Autowired
    private EntryService entryService;
    @Autowired
    private GeneralUtils utils;
    @Autowired
    private MainLayout mainLayout;
    @Autowired
    private EntryFormDialog entryFormDialog;
    private Long id;
    private PaginatedGrid<EntryPojo> gridLayout;
    private DataProvider<EntryPojo, Void> dataProvider;

    public TrackLayout() {

    }

    @PostConstruct
    private void init() {


        configureDataProvider();
        configureTrackLayout(entryFormDialog);

        id = utils.getId(VaadinService.getCurrentRequest().getCookies());

        gridLayout = createGridLayout();

        add(gridLayout);

        HorizontalLayout buttonLayout = new HorizontalLayout();
        add(buttonLayout);
        buttonLayout.setWidthFull();

        new AddButton(buttonLayout, "Add New Entry", entryFormDialog);

        Button deleteEntryButton = createDeleteButton(gridLayout);
        deleteEntryButton.addClickListener(event -> {
            gridLayout.getSelectedItems().forEach(entryPojo -> entryService.delete(entryPojo.getId()));
            dataProvider.refreshAll();
        });
        buttonLayout.add(deleteEntryButton);

        entryFormDialog.setView(this);
        entryFormDialog.addOpenedChangeListener(event -> {
            if (!event.isOpened()) {
                dataProvider.refreshAll();
            }
        });

        this.entryFormDialog.setGrid(gridLayout);
        this.entryFormDialog.setId(id);
    }

    private void configureDataProvider() {

        dataProvider = DataProvider.fromCallbacks(
                query -> {
                    long offset = gridLayout == null || gridLayout.getPage() < 1 ? 0L : gridLayout.getPage() - 1;
                    List<EntryPojo> data = entryService.getAllAccountEntriesPagedPojo(id, offset, 15L);
                    return data.stream();
                }, query -> entryService.getAmountOfEntries(id));
    }

    private Button createDeleteButton(Grid<EntryPojo> grid) {
        Button deleteEntryButton = new Button();
        deleteEntryButton.setIcon(VaadinIcon.TRASH.create());
        deleteEntryButton.setIconAfterText(true);
        deleteEntryButton.addClickListener(event -> {
            if (grid.getSelectedItems().size() > 0) {
                grid.getSelectedItems().forEach(item -> entryService.delete(item.getId()));
                dataProvider.refreshAll();
            }

        });
        return deleteEntryButton;
    }

    private void configureTrackLayout(EntryFormDialog entryFormDialog) {
        add(entryFormDialog);
        this.setHeightFull();
        this.setPadding(true);
        this.setClassName("content-background");
    }

    private PaginatedGrid<EntryPojo> createGridLayout() {
        PaginatedGrid<EntryPojo> gridLayout = new PaginatedGrid<>();

        gridLayout.setDataProvider(dataProvider);
        gridLayout.setClassName("track-grid");
        gridLayout.setPageSize(15);
        gridLayout.setPaginatorSize(2);
        setFlexGrow(1, gridLayout);
        gridLayout.setSelectionMode(Grid.SelectionMode.MULTI);


        gridLayout.addColumn(EntryPojo::getEntryDate).setHeader("Entry Date");
        gridLayout.addColumn(EntryPojo::getCharacterName).setHeader("Character Name");
        gridLayout.addColumn(EntryPojo::getGarrisonResources).setHeader("Garrison Resources");
        gridLayout.addColumn(EntryPojo::getWarPaint).setHeader("War Paint");


        return gridLayout;
    }


    public void refresh() {
        this.gridLayout.getDataProvider().refreshAll();
    }
}
