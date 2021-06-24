package com.trix.wowgarrisontracker.frontEnd;

import com.trix.wowgarrisontracker.frontEnd.components.MoneyCustomField;
import com.trix.wowgarrisontracker.frontEnd.fragments.MainLayout;
import com.trix.wowgarrisontracker.model.CardsOfOmen;
import com.trix.wowgarrisontracker.services.interfaces.CardsOfOmenService;
import com.trix.wowgarrisontracker.utils.GeneralUtils;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import org.vaadin.klaudeta.PaginatedGrid;

import javax.annotation.PostConstruct;
import javax.annotation.security.PermitAll;


@UIScope
@PermitAll
@Route(value = "/route", layout = MainLayout.class)
public class CardsOfOmenView extends FlexLayout {

    private final static int PAGE_SIZE = 20;

    private final CardsOfOmenService cardsOfOmenService;
    private DataProvider<CardsOfOmen, Void> dataProvider;
    private Grid<CardsOfOmen> cardsOfOmenGrid;
    private FormLayout formLayout;
    private Long accountId;

    public CardsOfOmenView(CardsOfOmenService cardsOfOmenService) {
        this.cardsOfOmenService = cardsOfOmenService;
        accountId = GeneralUtils.getCurrentlyLoggedUserId();
    }

    @PostConstruct
    public void init() {

        configureLayout();

        configureDataProvider();

        configureGrid();
        VerticalLayout verticalLayout = new VerticalLayout(cardsOfOmenGrid);
        verticalLayout.setClassName(LayoutVariables.CARDS_GRID);
        verticalLayout.setWidth(null);

        configureEntryForm();


        add(verticalLayout);
        add(formLayout);
    }

    private void configureEntryForm() {

        formLayout = new FormLayout();
        formLayout.setClassName(LayoutVariables.FORM_LAYOUT);
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0",1));

        IntegerField amountOfCardsField = new IntegerField("Amount of cards");
        MoneyCustomField startingGold = new MoneyCustomField("Starting money");
        MoneyCustomField finishedGold = new MoneyCustomField("Ending money");



        formLayout.add(amountOfCardsField,startingGold, finishedGold);
    }

    private void configureGrid() {

        cardsOfOmenGrid = new Grid<>(CardsOfOmen.class);
        setFlexGrow(1, cardsOfOmenGrid);
        cardsOfOmenGrid.setClassName(LayoutVariables.CARDS_GRID);
        cardsOfOmenGrid.setHeightByRows(false);
        cardsOfOmenGrid.setItems(dataProvider);

    }


    private void configureDataProvider() {
        dataProvider = DataProvider.fromCallbacks(query -> {
            int offset = query.getOffset();
            return cardsOfOmenService.findAllByAccountIdPaged(accountId, offset,query.getLimit()).stream();
        }, query -> cardsOfOmenService.getAmountOfEntries(accountId));
    }


    private void configureLayout() {
        setSizeFull();
        setClassName(LayoutVariables.PRIMARY_BACKGROUND);
        addClassName(LayoutVariables.WRAPPING);
    }

}
