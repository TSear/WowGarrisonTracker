package com.trix.wowgarrisontracker.frontEnd;

import com.trix.wowgarrisontracker.frontEnd.fragments.MainLayout;
import com.trix.wowgarrisontracker.pojos.AuctionPojo;
import com.trix.wowgarrisontracker.pojos.AuctionPojoWrapper;
import com.trix.wowgarrisontracker.services.interfaces.AuctionService;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.List;

@CssImport(value="./auctionHouse.css", themeFor = "vaadin-grid")
@Route(value = "auctionHouse", layout = MainLayout.class)

public class AuctionHouse extends FlexLayout {

    @Autowired
    private AuctionService auctionService;

    public AuctionHouse() {

    }

    @PostConstruct
    private void init() {

        configureMainView();

        List<AuctionPojoWrapper> data = auctionService.findAllAuctions();

        data.stream().forEach(item -> createTableGrid(item));

    }

    private void configureMainView() {
        setSizeFull();
        setFlexWrap(FlexWrap.WRAP);
        setAlignItems(Alignment.BASELINE);
        setJustifyContentMode(JustifyContentMode.CENTER);
        getStyle().set("overflow-y", "auto");
    }

    private VerticalLayout createTableWrapper(String tableLabelText) {
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setHeight("70%");
        verticalLayout.getStyle().set("flex-grow","1");
        verticalLayout.getStyle().set("max-width", "500px");
        verticalLayout.getStyle().set("min-width", "300px");
        Label label = new Label(tableLabelText);
        label.getStyle().set("text-align","center");
        label.getStyle().set("font-size", "1.6em");
        label.setWidthFull();

        verticalLayout.add(label);
        this.add(verticalLayout);

        return verticalLayout;
    }

    private void createTableGrid(AuctionPojoWrapper auctionData){
        auctionData.getInfo().sort((o1,o2) -> o1.getUnitPrice().compareTo(o2.getUnitPrice()));
        Grid<AuctionPojo> auctionPojoGrid = new Grid<>();
        auctionPojoGrid.setWidthFull();
        VerticalLayout table = createTableWrapper(auctionData.getItemName());
        auctionPojoGrid.setItems(auctionData.getInfo());
        auctionPojoGrid.addColumn(AuctionPojo::getPrice).setHeader("Price").setComparator((o1, o2)-> o1.getUnitPrice().compareTo(o2.getUnitPrice()));
        auctionPojoGrid.addColumn(AuctionPojo::getQuantity).setHeader("Quantity");

        table.add(auctionPojoGrid);

    }
}
