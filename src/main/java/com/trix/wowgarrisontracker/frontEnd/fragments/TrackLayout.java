package com.trix.wowgarrisontracker.frontEnd.fragments;

import com.trix.wowgarrisontracker.frontEnd.EntryFormDialog;
import com.trix.wowgarrisontracker.pojos.EntryPojo;
import com.trix.wowgarrisontracker.services.interfaces.EntryService;
import com.trix.wowgarrisontracker.utils.GeneralUtils;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;

import javax.annotation.PostConstruct;

@Profile("vaadin")
@CssImport(value = "./grid.css", themeFor = "vaadin-grid")
@UIScope
@Route(value = "", layout = MainLayout.class)
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

    public TrackLayout() {

    }

    @PostConstruct
    private void init() {


        configureTrackLayout(entryFormDialog);

        id = utils.getId(VaadinService.getCurrentRequest().getCookies());

        Grid<EntryPojo> gridLayout = createGridLayout(entryService, id);

        this.entryFormDialog.setGrid(gridLayout);
        this.entryFormDialog.setId(id);

        add(gridLayout);

        HorizontalLayout buttonLayout = new HorizontalLayout();
        add(buttonLayout);
        buttonLayout.setWidthFull();

        Button addEntryButton = createAddEntryButton();
        buttonLayout.add(addEntryButton);
        buttonLayout.setFlexGrow(1, addEntryButton);

        addEntryButton.addClickListener(event -> entryFormDialog.open());

        Button deleteEntryButton = createDeleteButton(gridLayout);

        buttonLayout.add(deleteEntryButton);

    }

    private Button createDeleteButton(Grid<EntryPojo> grid) {
        Button deleteEntryButton = new Button();
        deleteEntryButton.setIcon(VaadinIcon.TRASH.create());
        deleteEntryButton.setIconAfterText(true);
        deleteEntryButton.addClickListener(event -> {
            if (grid.getSelectedItems().size() > 0) {
                grid.getSelectedItems().stream().forEach(item -> entryService.delete(item.getId()));
                grid.setItems(entryService.accountEntriesConvertedToPojoList(id));
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

    private Grid<EntryPojo> createGridLayout(EntryService entryService, Long id) {
        Grid<EntryPojo> gridLayout = new Grid<>();
        gridLayout.setItems(entryService.accountEntriesConvertedToPojoList(id));
        gridLayout.setClassName("track-grid");

        gridLayout.addColumn(EntryPojo::getEntryDate).setHeader("Entry Date");
        gridLayout.addColumn(EntryPojo::getCharacterName).setHeader("Character Name");
        gridLayout.addColumn(EntryPojo::getGarrisonResources).setHeader("Garrison Resources");
        gridLayout.addColumn(EntryPojo::getWarPaint).setHeader("War Paint");
        return gridLayout;
    }

    private Button createAddEntryButton() {
        Button addEntryButton = new Button("Add new entry");
        addEntryButton.setWidthFull();
        addEntryButton.addClassName("add-button");
        return addEntryButton;
    }
}
