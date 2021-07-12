package com.trix.wowgarrisontracker.frontEnd.fragments;

import com.trix.wowgarrisontracker.frontEnd.LayoutVariables;
import com.trix.wowgarrisontracker.frontEnd.components.CenteredHeader;
import com.trix.wowgarrisontracker.frontEnd.components.CustomHr;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.springframework.stereotype.Component;


@Component
public class About extends VerticalLayout {

    private String paragraphText = "Garrison tracker as the name suggests is web application for people who spend their" +
            " time in the garrison making money. Of course if you don't want to track the income from your cards or" +
            " how much resource you are getting each day then this app won't help you much." +
            " But if you like to see how much money, resources you get then this app is for you." +
            " I also offer handy auction house, so you don't have to look for items on Undermine Journal or go to" +
            " auction house in game.";

    private String resouresParagraphText = "On resources page you can track how much garrison resources and war paint" +
            " you got on each character. To create a new entry you simply click 'Add new entry' button or right click " +
            "on grid and click 'Create new entry'. Then a dialog will open where you have to enter your character, " +
            "garrison resources and war paint. After you click 'Create entry' new entry will appear on the grid. You " +
            "can also delete entry by checking checkboxes on the left of the grid and clicking trash button or using " +
            "right click menu.";

    private String cardsOfOmenParagraphText = "On cards of omen page you can track your money from cards." +
            " The 'Cards opened'  field speaks for itself, here you insert how much cards you opened." +
            " Next field 'Money you started with' makes that you don't have to calculate yourself how much money you got." +
            " You just fill these fields with money you had before opening. " +
            "And 'Money you ended with' field you fill with the money after opening." +
            " These two fields makes that you can clearly see how much money you got.";

    private String charactersParagraphText = "On Characters page you can see all your characters and how much resources" +
            " they made for you. This page is useful for you if you want to see what character have the best setup for" +
            " making resources. To create new character you simply open the context menu via right clicking on the grid" +
            " or click 'Create new character'. Same with deleting, just select what character you want to delete and open" +
            " context menu or click trash button on the bottom.";

    private String statisticsParagraphText = "Statistics page is one of the more useful pages in my app." +
            " Here you can track things like total garrison resources, war paint, average resources per day." +
            " The day statistics works based on your resources entries. What does it mean?" +
            " For example if you create three entries on, let's say 4th April," +
            " day statistic will say '1' and then you create entry on 7th April," +
            " the statistic will say '2'.";

    private String auctionHouseParagraphText = "The auction house page simply shows you the prices of items on the server" +
            " you chose while creating account. Currently, there is not a lot of functionality" +
            ", but I have some plans to improve this page. Currently, there are only few items shown and there is simple" +
            " reason for this. I have limited storage space. So currently users cannot add new items." +
            " But if you really want specific item you can just send me a email and I will add it (my email pietrzyk.jakub001@gmail.com)";

    private String settingsParagraphText = "And last one is settings page." +
            " What can I say about it is just simple settings page." +
            " You can change your server, password and email." +
            " The 'Email notifications' feature is not yet implemented. ";

    private boolean placeOnLeft = true;

    public About() {

        FlexLayout aboutBox = createAboutBox("About", paragraphText, null);

        FlexLayout resourcesBox = createAboutBox("Resources",
                resouresParagraphText,
                new Image("/img/resources.png", "Resources image not found"));

        FlexLayout cardsOfOmenBox = createAboutBox("Cards Of Omen",
                cardsOfOmenParagraphText,
                new Image("/img/cardsOfOmen.png", "Cards of omen image not found"));

        FlexLayout charactersBox = createAboutBox("Characters",
                charactersParagraphText,
                new Image("/img/characters.png", "Characters image not found"));

        FlexLayout statisticsBox = createAboutBox("Statistics",
                statisticsParagraphText,
                new Image("/img/statistics.png", "Statistics image not found"));

        FlexLayout auctionHouseBox = createAboutBox("Auction House",
                auctionHouseParagraphText,
                new Image("/img/auctionHouse.png", "Auction house image not found"));

        FlexLayout settingsBox = createAboutBox("Settings",
                settingsParagraphText,
                new Image("/img/settings.png", "Settings image not found"));

        add(aboutBox, new CustomHr(),
                resourcesBox, new CustomHr(),
                cardsOfOmenBox, new CustomHr(),
                charactersBox, new CustomHr(),
                statisticsBox, new CustomHr(),
                auctionHouseBox, new CustomHr(),
                settingsBox, new CustomHr());

    }

    private FlexLayout createFlexLayout() {
        FlexLayout flexLayout = new FlexLayout();
        flexLayout.setFlexDirection(FlexLayout.FlexDirection.ROW);
        flexLayout.setFlexWrap(FlexLayout.FlexWrap.WRAP);
        flexLayout.setSizeFull();
        flexLayout.setAlignContent(FlexLayout.ContentAlignment.CENTER);
        flexLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        return flexLayout;
    }

    private FlexLayout createAboutBox(String headerText, String paragraph, Image image) {
        FlexLayout flexLayout = createFlexLayout();

        CenteredHeader centeredHeader = new CenteredHeader(headerText);
        Paragraph resourcesParagraph = new Paragraph(paragraph);
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.add(centeredHeader, resourcesParagraph);
        verticalLayout.addClassName(LayoutVariables.PARAGRAPH_ABOUT);
        verticalLayout.setJustifyContentMode(JustifyContentMode.CENTER);

        if (image != null) {
            image.setClassName(LayoutVariables.IMAGE);
            flexLayout.add(verticalLayout, image);
        } else {
            flexLayout.add(verticalLayout);
        }


        return flexLayout;
    }


}
