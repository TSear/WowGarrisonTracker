package com.trix.wowgarrisontracker.frontEnd.views.cardsOfOmen;

import com.trix.wowgarrisontracker.frontEnd.LayoutVariables;
import com.trix.wowgarrisontracker.frontEnd.views.statistics.StatisticsView;
import com.trix.wowgarrisontracker.frontEnd.components.MoneyCustomField;
import com.trix.wowgarrisontracker.frontEnd.fragments.MainLayout;
import com.trix.wowgarrisontracker.frontEnd.interfaces.Refreshable;
import com.trix.wowgarrisontracker.model.CardsOfOmen;
import com.trix.wowgarrisontracker.pojos.CardsOfOmenEntryPojo;
import com.trix.wowgarrisontracker.services.interfaces.CardsOfOmenService;
import com.trix.wowgarrisontracker.utils.GeneralUtils;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.validator.IntegerRangeValidator;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;

import javax.annotation.PostConstruct;
import javax.annotation.security.PermitAll;
import java.time.LocalDate;


@UIScope
@PermitAll
@PageTitle("Cards Of Omen")
@Route(value = "/route", layout = MainLayout.class)
public class CardsOfOmenView extends FlexLayout {

    private final static int PAGE_SIZE = 20;

    private final CardsOfOmenService cardsOfOmenService;
    private DataProvider<CardsOfOmen, Void> dataProvider;
    private Binder<CardsOfOmenEntryPojo> pojoBinder;
    private CardsOfOmenEntryPojo pojo;
    private Grid<CardsOfOmen> cardsOfOmenGrid;
    private FormLayout formLayout;
    private Long accountId;
    private final Refreshable statistics;

    public CardsOfOmenView(CardsOfOmenService cardsOfOmenService, StatisticsView statistics) {
        this.cardsOfOmenService = cardsOfOmenService;
        accountId = GeneralUtils.getCurrentlyLoggedUserId();
        this.statistics = statistics;
    }

    @PostConstruct
    public void init() {

        pojo = new CardsOfOmenEntryPojo();
        pojoBinder = new Binder<>();

        configureLayout();

        configureDataProvider();

        configureGrid();
        VerticalLayout verticalLayout = new VerticalLayout(cardsOfOmenGrid);
        verticalLayout.setClassName(LayoutVariables.CARDS_GRID);
        verticalLayout.setWidth(null);

        configureContextMenuForGrid();

        configureEntryForm();

        pojoBinder.readBean(pojo);

        add(verticalLayout);
        add(formLayout);
    }

    private void configureContextMenuForGrid() {

        GridContextMenu<CardsOfOmen> contextMenu = cardsOfOmenGrid.addContextMenu();

        contextMenu.addItem("Remove Entry", event -> {
            if(event.getGrid().getSelectedItems().size()>0){
                event.getGrid().getSelectedItems().forEach(cardsOfOmenService::delete);
                dataProvider.refreshAll();
            }else{
                Notification notification = new Notification("You need to select entry to delete", 5000);
                notification.open();
            }
        });

    }

    private void configureEntryForm() {

        formLayout = new FormLayout();
        formLayout.setClassName(LayoutVariables.FORM_LAYOUT);
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1));

        IntegerField amountOfCardsField = new IntegerField("Amount of cards opened");
        MoneyCustomField startingGold = new MoneyCustomField("Money you started with");
        MoneyCustomField finishedGold = new MoneyCustomField("Money you ended with");
        Button addButton = createAddButton();
        Button clearButton = createClearButton();
        HorizontalLayout buttonLayout = new HorizontalLayout(addButton, clearButton);
        buttonLayout.setFlexGrow(1, addButton);
        amountOfCardsField.setStep(1);
        amountOfCardsField.setHasControls(true);

        pojoBinder.forField(amountOfCardsField)
                .asRequired("Please fill this field")
                .withValidator(new IntegerRangeValidator("Value must be between <0,100_000_000>"
                        , 0, 100_000_000))
                .bind(CardsOfOmenEntryPojo::getAmountOfCards, CardsOfOmenEntryPojo::setAmountOfCards);

        pojoBinder.forField(startingGold)
                .asRequired("Please fill empty fields")
                .bind(CardsOfOmenEntryPojo::getStartingMoney, CardsOfOmenEntryPojo::setStartingMoney);

        pojoBinder.forField(finishedGold)
                .asRequired("Please fill empty fields")
                .withValidator(money -> money.compareTo(startingGold.getValue())>0
                        ,"Check if starting gold is smaller than ending money")
                .bind(CardsOfOmenEntryPojo::getEndingMoney, CardsOfOmenEntryPojo::setEndingMoney);

        formLayout.add(amountOfCardsField, startingGold, finishedGold, buttonLayout);
    }

    private Button createAddButton() {
        Button button = new Button("Create entry");
        button.addClickListener(buttonClickEvent -> {
            try {
                pojoBinder.writeBean(pojo);
                CardsOfOmen cards = new CardsOfOmen();

                cards.setAmountOfCards(pojo.getAmountOfCards());
                cards.setMoneyFromCards(pojo.getEndingMoney().subtract(pojo.getStartingMoney()));
                cards.setAccount(GeneralUtils.getCustomUserPrincipal().getAccount());
                cards.setLocalDate(LocalDate.now());

                cardsOfOmenService.save(cards);
                dataProvider.refreshAll();
                statistics.refresh();

            } catch (ValidationException e) {
                //TODO exception handling
                System.out.println("Validation failed");
            }
        });
        return button;
    }

    private Button createClearButton() {
        Button button = new Button("Clear form");
        button.addClickListener(buttonClickEvent -> pojoBinder.readBean(new CardsOfOmenEntryPojo()));
        button.addClassName(LayoutVariables.SECONDARY_BUTTON);
        return button;
    }


    private void configureGrid() {

        cardsOfOmenGrid = new Grid<>();
        setFlexGrow(1, cardsOfOmenGrid);
        cardsOfOmenGrid.setClassName(LayoutVariables.CARDS_GRID);
        cardsOfOmenGrid.setHeightByRows(false);
        cardsOfOmenGrid.setItems(dataProvider);

        cardsOfOmenGrid.addColumn(CardsOfOmen::getLocalDate)
                .setHeader("Entry date")
                .setKey("date").setFlexGrow(10);

        cardsOfOmenGrid.addColumn(CardsOfOmen::getAmountOfCards)
                .setHeader("Amount of cards")
                .setKey("amountOfCards").setFlexGrow(10);

        cardsOfOmenGrid.addColumn(CardsOfOmen -> CardsOfOmen.getMoneyFromCards().getFormattedValues()[0])
                .setHeader("Gold")
                .setKey("gold");

        cardsOfOmenGrid.addColumn(CardsOfOmen -> CardsOfOmen.getMoneyFromCards().getFormattedValues()[1])
                .setHeader("Silver")
                .setKey("silver");

        cardsOfOmenGrid.addColumn(CardsOfOmen -> CardsOfOmen.getMoneyFromCards().getFormattedValues()[2])
                .setHeader("Copper")
                .setKey("copper");

        cardsOfOmenGrid.getColumns().forEach(cardsOfOmenColumn -> cardsOfOmenColumn.setAutoWidth(true));

    }


    private void configureDataProvider() {
        dataProvider = DataProvider.fromCallbacks(query -> {
            int offset = query.getOffset();
            return cardsOfOmenService.findAllByAccountIdPaged(accountId, offset, query.getLimit()).stream();
        }, query -> cardsOfOmenService.getAmountOfEntries(accountId));
    }


    private void configureLayout() {
        setSizeFull();
        setClassName(LayoutVariables.PRIMARY_BACKGROUND);
        addClassName(LayoutVariables.WRAPPING);
    }

}
