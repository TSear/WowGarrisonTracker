package com.trix.wowgarrisontracker.frontEnd;

import com.trix.wowgarrisontracker.frontEnd.fragments.MainLayout;
import com.trix.wowgarrisontracker.model.Account;
import com.trix.wowgarrisontracker.services.implementation.StatisticsServiceImpl;
import com.trix.wowgarrisontracker.services.interfaces.AccountService;
import com.trix.wowgarrisontracker.services.interfaces.EntryService;
import com.trix.wowgarrisontracker.services.interfaces.StatisticsService;
import com.trix.wowgarrisontracker.utils.GeneralUtils;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@UIScope
@Route(value = "statistics", layout = MainLayout.class)
public class Statistics extends FlexLayout {


    @Autowired
    private AccountService accountService;
    @Autowired
    private EntryService entryService;
    @Autowired
    private GeneralUtils utils;

    private StatisticsService statisticsService;

    public Statistics() {
    }

    @Autowired
    public void setStatisticsService(StatisticsService statisticsService){
        this.statisticsService = statisticsService;
    }

    @PostConstruct
    private void init() {

        setSizeFull();
        setFlexWrap(FlexWrap.WRAP);
        setClassName("statistics-container");
        getStyle().set("padding", "15px");
        getStyle().set("box-sizing","border-box");


        Long id = utils.getCurrentlyLoggedUserId();

        Account account = accountService.findById(id);
        Long days = (long) entryService.getAmountOfDays(id);

        Div totalResources = createStatisticDiv("Total Garrison Resources", account.getTotalGarrisonResources() + "");
        Div totalWarPaint = createStatisticDiv("Total War Paint", account.getTotalWarPaint()+"");
        Div averageWarPaintPD = createStatisticDiv("Average War Paint Per Day", account.getTotalWarPaint() / Math.max(days,1) + "");
        Div averageGarrisonResourcesPD = createStatisticDiv("Average Garrison Resources", account.getTotalGarrisonResources() / Math.max(days,1) + "");
        Div amountOfEntries = createStatisticDiv("Entries", account.getAmountOfEntries() +"");
        Div amountOfDays = createStatisticDiv("Days", days+"");

        add(totalResources,totalWarPaint,averageGarrisonResourcesPD,averageWarPaintPD,amountOfEntries,amountOfDays);
    }

    private Div createStatisticDiv(String label, String value) {
        Div tmp = new Div();
        tmp.setClassName("statistic-div");
        VerticalLayout verticalLayout = new VerticalLayout();
        tmp.add(verticalLayout);

        Label divDescription = new Label(label);
        Label divValue = new Label(value);
        divDescription.setClassName("statistic-description");
        divValue.setClassName("statistic-value");
        verticalLayout.add(divDescription, divValue);

        divDescription.setWidthFull();
        divValue.setWidthFull();

        return tmp;
    }
}
