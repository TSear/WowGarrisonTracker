package com.trix.wowgarrisontracker.frontEnd.views.statistics;

import com.trix.wowgarrisontracker.frontEnd.LayoutVariables;
import com.trix.wowgarrisontracker.frontEnd.fragments.MainLayout;
import com.trix.wowgarrisontracker.frontEnd.interfaces.Refreshable;
import com.trix.wowgarrisontracker.pojos.StatisticsPojo;
import com.trix.wowgarrisontracker.services.interfaces.StatisticsService;
import com.trix.wowgarrisontracker.utils.GeneralUtils;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.security.PermitAll;

@Component
@UIScope
@PermitAll
@PageTitle("Statistics")
@Route(value = "statistics", layout = MainLayout.class)
public class StatisticsView extends FlexLayout implements Refreshable {


    private final StatisticsService statisticsService;

    private Long id;

    private final Label totalResourcesLabel;
    private final Label totalWarPaintLabel;
    private final Label averageWarPaintPDLabel;
    private final Label averageResourcesPDLabel;
    private final Label amountOfEntriesLabel;
    private final Label amountOfDaysLabel;
    private final Label totalGoldLabel;
    private final Label cardsOpenedLabel;


    public StatisticsView(StatisticsService statisticsService) {
        totalResourcesLabel = new Label();
        totalWarPaintLabel = new Label();
        averageResourcesPDLabel = new Label();
        averageWarPaintPDLabel = new Label();
        amountOfEntriesLabel = new Label();
        amountOfDaysLabel = new Label();
        totalGoldLabel = new Label();
        cardsOpenedLabel = new Label();
        this.statisticsService = statisticsService;
    }

    @PostConstruct
    private void init() {

        configureFrame();

        id = GeneralUtils.getCurrentlyLoggedUserId();

        updateLabels();

        Div totalResources = createStatisticDiv("Total Garrison Resources", totalResourcesLabel);
        Div totalWarPaint = createStatisticDiv("Total War Paint", totalWarPaintLabel);
        Div averageWarPaintPD = createStatisticDiv("Average War Paint Per Day", averageWarPaintPDLabel);
        Div averageGarrisonResourcesPD = createStatisticDiv("Average Garrison Resources", averageResourcesPDLabel);
        Div amountOfEntries = createStatisticDiv("Entries", amountOfEntriesLabel);
        Div amountOfDays = createStatisticDiv("Days", amountOfDaysLabel);
        Div totalGoldFromCards = createStatisticDiv("Gold from cards", totalGoldLabel);
        Div cardsOpened = createStatisticDiv("Cards opened", cardsOpenedLabel);

        add(totalResources, totalWarPaint, averageGarrisonResourcesPD, averageWarPaintPD, amountOfEntries, amountOfDays,
                totalGoldFromCards,cardsOpened);
    }

    private void configureFrame() {
        setFlexWrap(FlexWrap.WRAP);
        setClassName("statistics-container");
        getStyle().set("padding", "15px");
        getStyle().set("box-sizing", "border-box");
        addClassName(LayoutVariables.PRIMARY_BACKGROUND);
    }

    private void updateLabels() {
        StatisticsPojo statistics = statisticsService.getAllStatistics(id);
        totalResourcesLabel.setText(statistics.getTotalGarrisonResources() + "");
        totalWarPaintLabel.setText(statistics.getTotalWarPaint() + "");
        averageWarPaintPDLabel.setText(statistics.getAverageDailyWarPaint() + "");
        averageResourcesPDLabel.setText(statistics.getAverageDailyGarrisonResources() + "");
        amountOfEntriesLabel.setText(statistics.getAmountOfEntries() + "");
        amountOfDaysLabel.setText(statistics.getDays() + "");
        totalGoldLabel.setText(statistics.getTotalGold() + "");
        cardsOpenedLabel.setText(statistics.getTotalCardsOpened() + "");

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
    public void refresh() {
        updateLabels();
    }
}
