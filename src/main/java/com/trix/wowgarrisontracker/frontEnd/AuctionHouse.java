package com.trix.wowgarrisontracker.frontEnd;

import com.trix.wowgarrisontracker.frontEnd.fragments.MainLayout;
import com.trix.wowgarrisontracker.frontEnd.interfaces.Refreshable;
import com.trix.wowgarrisontracker.model.Server;
import com.trix.wowgarrisontracker.pojos.AuctionPojo;
import com.trix.wowgarrisontracker.pojos.AuctionPojoWrapper;
import com.trix.wowgarrisontracker.services.interfaces.AuctionService;
import com.trix.wowgarrisontracker.utils.GeneralUtils;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import com.vaadin.flow.spring.annotation.VaadinSessionScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Comparator;
import java.util.List;

//@CssImport(value="./auctionHouse.css", themeFor = "vaadin-grid")
@Component
@UIScope
@Route(value = "auctionHouse", layout = MainLayout.class)
public class AuctionHouse extends FlexLayout implements Refreshable {

    @Autowired
    private AuctionService auctionService;

    public AuctionHouse() {

    }

    @PostConstruct
    private void init() {

        configureMainView();
        int connectedServerId = GeneralUtils.getUserSettings().getServer().getConnectedServersModel().getConnectedServerId();
        Server server = GeneralUtils.getUserSettings().getServer();

        H2 header = createHeader(server);

        List<AuctionPojoWrapper> data = auctionService.findByConnectedServerIdPojo(connectedServerId);

        add(header);
        data.forEach(this::createTableGrid);

    }

    private H2 createHeader(Server server) {
        H2 header = new H2(server.toString());
        header.setWidthFull();
        header.getStyle().set("text-align","center");
        return header;
    }

    private void configureMainView() {
        setSizeFull();
        setFlexWrap(FlexWrap.WRAP);
        setAlignItems(Alignment.BASELINE);
        setJustifyContentMode(JustifyContentMode.CENTER);
        getStyle().set("overflow-y", "auto");
        setClassName(LayoutVariables.PRIMARY_BACKGROUND);
    }

    private VerticalLayout createTableWrapper(String tableLabelText) {
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setHeight("70%");
        verticalLayout.getStyle().set("flex-grow", "1");
        verticalLayout.getStyle().set("max-width", "500px");
        verticalLayout.getStyle().set("min-width", "300px");
        Label label = new Label(tableLabelText);
        label.getStyle().set("text-align", "center");
        label.getStyle().set("font-size", "1.6em");
        label.setWidthFull();

        verticalLayout.add(label);
        this.add(verticalLayout);

        return verticalLayout;
    }

    private void createTableGrid(AuctionPojoWrapper auctionData) {
        auctionData.getInfo().sort(Comparator.comparing(AuctionPojo::getUnitPrice));
        Grid<AuctionPojo> auctionPojoGrid = new Grid<>();
        auctionPojoGrid.setWidthFull();
        VerticalLayout table = createTableWrapper(auctionData.getItemName());
        auctionPojoGrid.setItems(auctionData.getInfo());
        auctionPojoGrid.addColumn(AuctionPojo::getPrice).setHeader("Price").setComparator(Comparator.comparing(AuctionPojo::getUnitPrice));
        auctionPojoGrid.addColumn(AuctionPojo::getQuantity).setHeader("Quantity");

        table.add(auctionPojoGrid);

    }


    @Override
    public void refresh() {
        //TODO Need to change it to data provider or something else.
        UI.getCurrent().getPage().reload();
    }
}
