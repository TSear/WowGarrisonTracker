package com.trix.wowgarrisontracker.frontEnd;

import com.trix.wowgarrisontracker.converters.OptionsDTOToOptions;
import com.trix.wowgarrisontracker.converters.OptionsToOptionsDTO;
import com.trix.wowgarrisontracker.enums.Regions;
import com.trix.wowgarrisontracker.frontEnd.fragments.MainLayout;
import com.trix.wowgarrisontracker.frontEnd.interfaces.Refreshable;
import com.trix.wowgarrisontracker.model.CustomUserDetails;
import com.trix.wowgarrisontracker.model.Options;
import com.trix.wowgarrisontracker.model.Server;
import com.trix.wowgarrisontracker.pojos.OptionsDTO;
import com.trix.wowgarrisontracker.services.interfaces.OptionsService;
import com.trix.wowgarrisontracker.services.interfaces.ServerService;
import com.trix.wowgarrisontracker.utils.GeneralUtils;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import com.vaadin.flow.spring.annotation.VaadinSessionScope;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.annotation.PostConstruct;
import javax.annotation.security.PermitAll;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CssImport(value = "/css/settings.css", themeFor = "vaadin-form-layout")
@UIScope
@PermitAll
@Route(value = "settings", layout = MainLayout.class)
public class Settings extends VerticalLayout {

    private final FormLayout formLayout;
    private final Binder<OptionsDTO> optionsDTOBinder;
    private final OptionsDTO optionsDTO;
    private final Notification savedNotification;
    private final OptionsService optionsService;
    private final List<Server> serverList;
    private final OptionsDTOToOptions optionsDTOToOptions;
    private final Refreshable auctionHouse;
    private Map<String, List<Server>> groupedServers;

public Settings(OptionsService optionsService, ServerService serverService, OptionsDTOToOptions optionsDTOToOptions,
                AuctionHouse auctionHouse) {
        this.optionsService = optionsService;
        this.optionsDTOToOptions = optionsDTOToOptions;
        serverList = serverService.getServers();
        OptionsToOptionsDTO optionsToOptionsDTO = new OptionsToOptionsDTO();
        this.formLayout = new FormLayout();
        this.optionsDTOBinder = new Binder<>();
        this.optionsDTO = optionsToOptionsDTO.convert(GeneralUtils.getUserSettings());
        this.savedNotification = new Notification("Options were saved", 2000);
        this.auctionHouse = auctionHouse;
    }

    @PostConstruct
    private void init() {

        configureFrame();

        groupedServers = serverList.stream().collect(Collectors.groupingBy(Server::getRegion));

        configureFormLayout();

        H2 titleLabel = new H2("Settings");
        titleLabel.setClassName("page-label");

        ComboBox<Server> serverNameComboBox = configureServerNameComboBox();

        ComboBox<Regions> regionComboBox = configureRegionComboBox(serverNameComboBox);

        Checkbox sendEmailNotificationsCheckBox = configureEmailNotificationsCheckBox();

        Button submitFormButton = configureSubmitFormButton();

        optionsDTOBinder.readBean(optionsDTO);

        formLayout.addFormItem(regionComboBox, "Region");
        formLayout.addFormItem(serverNameComboBox, "Server Name");
        formLayout.addFormItem(sendEmailNotificationsCheckBox, "Send email notifications");
        formLayout.add(submitFormButton);

        add(titleLabel, formLayout);

    }

    private Button configureSubmitFormButton() {
        Button submitFormButton = new Button("Submit");

        submitFormButton.setClassName("margin-top-big");
        submitFormButton.addClickListener(event -> {
            try {
                optionsDTOBinder.writeBean(optionsDTO);
                Options optionsTmp = optionsDTOToOptions.convert(optionsDTO);
                optionsService.save(optionsTmp);
                //TODO for now this is refreshing logged user options without logging out. Will need to find better way
                CustomUserDetails tmp = GeneralUtils.getCustomUserPrincipal();
                tmp.getAccount().setOptions(optionsDTOToOptions.convert(optionsDTO));
                SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(tmp, null, new ArrayList<>()));
                savedNotification.open();
                auctionHouse.refresh();
            } catch (ValidationException e) {
                System.out.println(e.getMessage());
            }
        });
        return submitFormButton;
    }

    private Checkbox configureEmailNotificationsCheckBox() {
        Checkbox sendEmailNotificationsCheckBox = new Checkbox();
        optionsDTOBinder.forField(sendEmailNotificationsCheckBox).bind(OptionsDTO::isReceiveEmailNotifications, OptionsDTO::setReceiveEmailNotifications);
        return sendEmailNotificationsCheckBox;
    }

    private ComboBox<Server> configureServerNameComboBox() {
        ComboBox<Server> serverNameComboBox = new ComboBox<>();
        serverNameComboBox.setWidthFull();
        serverNameComboBox.setItems(serverList);
        serverNameComboBox.setItemLabelGenerator(Server::toString);
        serverNameComboBox.setAllowCustomValue(false);
        optionsDTOBinder.forField(serverNameComboBox).bind(OptionsDTO::getServer, OptionsDTO::setServer);
        return serverNameComboBox;
    }

    private ComboBox<Regions> configureRegionComboBox(ComboBox<Server> serverNameComboBox) {
        ComboBox<Regions> regionComboBox = new ComboBox<>();
        regionComboBox.setItems(Regions.values());
        regionComboBox.setValue(Arrays.stream(Regions.values())
                .filter(regions1 -> regions1.getValue().equalsIgnoreCase(optionsDTO.getServer().getRegion()))
                .findFirst().orElse(Regions.values()[0]));
        regionComboBox.setAllowCustomValue(false);
        regionComboBox.addValueChangeListener(event -> {
            if (regionComboBox.getValue().getValue().equalsIgnoreCase("all")) {
                serverNameComboBox.setItems(serverList);
            }
            else {
                serverNameComboBox.setItems(groupedServers.get(regionComboBox.getValue().getValue()));
            }
        });
        return regionComboBox;
    }

    private void configureFormLayout() {
        formLayout.setClassName("settings-layout");
        formLayout.setResponsiveSteps(
                new ResponsiveStep("0", 1));
    }

    private void configureFrame() {
        this.setSizeFull();
        this.setClassName("background");
        addClassName(LayoutVariables.PRIMARY_BACKGROUND);
    }
}
