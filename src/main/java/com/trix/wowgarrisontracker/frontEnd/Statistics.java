package com.trix.wowgarrisontracker.frontEnd;

import com.trix.wowgarrisontracker.frontEnd.fragments.MainLayout;
import com.trix.wowgarrisontracker.model.Account;
import com.trix.wowgarrisontracker.pojos.StatisticsPojo;
import com.trix.wowgarrisontracker.services.interfaces.AccountService;
import com.trix.wowgarrisontracker.services.interfaces.EntryService;
import com.trix.wowgarrisontracker.services.interfaces.StatisticsService;
import com.trix.wowgarrisontracker.utils.GeneralUtils;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@UIScope
@Route(value = "statistics", layout = MainLayout.class)
public class Statistics extends FlexLayout implements BeforeEnterObserver {


    @Autowired
    private AccountService accountService;
    @Autowired
    private EntryService entryService;
    @Autowired
    private GeneralUtils utils;

    private StatisticsService statisticsService;

    private Account account;
    private Long id;

    private Label totalResourcesLabel;
    private Label totalWarPaintLabel;
    private Label averageWarPaintPDLabel;
    private Label averageResourcesPDLabel;
    private Label amountOfEntriesLabel;
    private Label amountOfDaysLabel;


    public Statistics() {
        totalResourcesLabel = new Label();
        totalWarPaintLabel = new Label();
        averageResourcesPDLabel = new Label();
        averageWarPaintPDLabel = new Label();
        amountOfEntriesLabel = new Label();
        amountOfDaysLabel = new Label();
    }

    @Autowired
    public void setStatisticsService(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @PostConstruct
    private void init() {

        setSizeFull();
        setFlexWrap(FlexWrap.WRAP);
        setClassName("statistics-container");
        getStyle().set("padding", "15px");
        getStyle().set("box-sizing", "border-box");

        updateAccount();

        updateLabels();

        Div totalResources = createStatisticDiv("Total Garrison Resources", totalResourcesLabel);
        Div totalWarPaint = createStatisticDiv("Total War Paint", totalWarPaintLabel);
        Div averageWarPaintPD = createStatisticDiv("Average War Paint Per Day", averageWarPaintPDLabel);
        Div averageGarrisonResourcesPD = createStatisticDiv("Average Garrison Resources", averageResourcesPDLabel);
        Div amountOfEntries = createStatisticDiv("Entries", amountOfEntriesLabel);
        Div amountOfDays = createStatisticDiv("Days",  amountOfDaysLabel);

        add(totalResources, totalWarPaint, averageGarrisonResourcesPD, averageWarPaintPD, amountOfEntries, amountOfDays);
    }

    private void updateLabels() {
        StatisticsPojo statistics = statisticsService.getAllStatistics(id);
        totalResourcesLabel.setText(statistics.getTotalGarrisonResources() + "");
        totalWarPaintLabel.setText(statistics.getTotalWarPaint() +"");
        averageWarPaintPDLabel.setText(statistics.getAverageDailyWarPaint()+"");
        averageResourcesPDLabel.setText(statistics.getAverageDailyGarrisonResources()+"");
        amountOfEntriesLabel.setText(statistics.getAmountOfEntries() + "");
        amountOfDaysLabel.setText(statistics.getDays() + "");

    }

    private Div createStatisticDiv(String label, Label valueLabel) {
        Div tmp = new Div();
        tmp.setClassName("statistic-div");

        VerticalLayout verticalLayout = new VerticalLayout();
        tmp.add(verticalLayout);

        Label divDescription = new Label(label);

        divDescription.setClassName("statistic-description");
        valueLabel.setClassName("statistic-value");
        verticalLayout.add(divDescription, valueLabel);

        divDescription.setWidthFull();
        valueLabel.setWidthFull();

        return tmp;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
    }

    public void update() {
        updateLabels();
    }

    private void updateAccount() {
        id = GeneralUtils.getCurrentlyLoggedUserId();
        account = accountService.findById(id);

    }
}
