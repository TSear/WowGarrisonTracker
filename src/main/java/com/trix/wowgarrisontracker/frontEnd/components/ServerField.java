package com.trix.wowgarrisontracker.frontEnd.components;

import com.trix.wowgarrisontracker.enums.Regions;
import com.trix.wowgarrisontracker.model.Server;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.formlayout.FormLayout;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ServerField extends CustomField<Server> {

    private final List<Server> serverList;
    private final Map<String, List<Server>> groupedServers;
    private final ComboBox<Regions> regionComboBox;
    private final ComboBox<Server> serverNameComboBox;

    public ServerField(List<Server> serverList) {

        this.serverList = serverList;

        groupedServers = serverList.stream().collect(Collectors.groupingBy(Server::getRegion));

        FormLayout formLayout = new FormLayout();
        formLayout.setWidthFull();
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0",1));

        serverNameComboBox = configureServerNameComboBox();

        regionComboBox = configureRegionComboBox(serverNameComboBox);

        formLayout.addFormItem(regionComboBox, "Region");
        formLayout.addFormItem(serverNameComboBox,"Server");

        add(formLayout);
    }

    private ComboBox<Server> configureServerNameComboBox() {
        ComboBox<Server> serverNameComboBox = new ComboBox<>();
        serverNameComboBox.setWidthFull();
        serverNameComboBox.setItems(serverList);
        serverNameComboBox.setItemLabelGenerator(Server::toString);
        serverNameComboBox.setAllowCustomValue(false);

        return serverNameComboBox;
    }

    private ComboBox<Regions> configureRegionComboBox(ComboBox<Server> serverNameComboBox) {
        ComboBox<Regions> regionComboBox = new ComboBox<>();
        regionComboBox.setItems(Regions.values());
        regionComboBox.setAllowCustomValue(false);

        regionComboBox.addValueChangeListener(event -> {
            if (regionComboBox.getValue().getValue().equalsIgnoreCase("all")) {
                serverNameComboBox.setItems(serverList);
            } else {
                serverNameComboBox.setItems(groupedServers.get(regionComboBox.getValue().getValue()));
            }
        });

        return regionComboBox;
    }

    @Override
    protected Server generateModelValue() {
        return serverNameComboBox.getValue();
    }

    @Override
    protected void setPresentationValue(Server newPresentationValue) {

        regionComboBox.setValue(Arrays.stream(Regions.values())
                .filter(regions1 -> regions1.getValue().equalsIgnoreCase(newPresentationValue.getRegion()))
                .findFirst()
                .orElse(Regions.values()[0]));

        serverNameComboBox.setValue(newPresentationValue);

    }
}
