package com.trix.wowgarrisontracker.frontEnd;

import com.trix.wowgarrisontracker.converters.OptionsDTOToOptions;
import com.trix.wowgarrisontracker.enums.Regions;
import com.trix.wowgarrisontracker.frontEnd.fragments.MainLayout;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CssImport(value = "./settings.css", themeFor = "vaadin-form-layout")
@UIScope
@Route(value = "settings", layout = MainLayout.class)
public class Settings extends VerticalLayout {

    private GeneralUtils generalUtils;
    private FormLayout formLayout;
    private Binder<OptionsDTO> optionsDTOBinder;
    private List<Server> serverList;
    private OptionsDTO optionsDTO;
    private OptionsService optionsService;
    private OptionsDTOToOptions optionsDTOToOptions;
    private Notification savedNotification;

    public Settings() {
        this.generalUtils = new GeneralUtils();
        this.formLayout = new FormLayout();
        this.optionsDTOBinder = new Binder<>();
        this.optionsDTO = generalUtils.getUserSettings();
        this.savedNotification = new Notification("Options were saved", 2000);
    }

    @Autowired
    public void setServers(ServerService serverService) {
        serverList = serverService.getServers();
    }

    @Autowired
    public void setOptionsService(OptionsService optionsService) {
        this.optionsService = optionsService;
    }

    @Autowired
    public void setOptionsDTOToOptions(OptionsDTOToOptions optionsDTOToOptions) {
        this.optionsDTOToOptions = optionsDTOToOptions;
    }

    @PostConstruct
    private void init() {

        this.setSizeFull();
        this.setClassName("background");

        List<String> regions = Arrays.stream(Regions.values()).map(Regions::getValue).collect(Collectors.toList());
        Map<String, List<Server>> groupedServers = serverList.stream().collect(Collectors.groupingBy(Server::getRegion));


        formLayout.setClassName("settings-layout");
        formLayout.setResponsiveSteps(
                new ResponsiveStep("0", 1));

        H2 titleLabel = new H2("Settings");
        ComboBox<String> regionComboBox = new ComboBox<>();
        ComboBox<Server> serverNameComboBox = new ComboBox<>();
        Checkbox sendEmailNotificationsCheckBox = new Checkbox();
        Button submitFormButton = new Button("Submit");

        serverNameComboBox.setWidthFull();

        titleLabel.setClassName("page-label");

        regionComboBox.setItems(regions);
        regionComboBox.setValue(regions.stream().filter(s -> s.equalsIgnoreCase(optionsDTO.getServer().getRegion())).findFirst().orElse("All"));
        regionComboBox.setAllowCustomValue(false);
        regionComboBox.addValueChangeListener(event -> {
            if (regionComboBox.getValue().equalsIgnoreCase("all"))
                serverNameComboBox.setItems(serverList);
            else
                serverNameComboBox.setItems(groupedServers.get(regionComboBox.getValue().toLowerCase()));
        });


        serverNameComboBox.setItems(serverList);
        serverNameComboBox.setItemLabelGenerator(Server::toString);
        serverNameComboBox.setAllowCustomValue(false);


        submitFormButton.setClassName("margin-top-big");
        submitFormButton.addClickListener(event -> {
            try {
                optionsDTOBinder.writeBean(optionsDTO);
                Options optionsTmp = optionsDTOToOptions.convert(optionsDTO);
                optionsService.save(optionsTmp);
                CustomUserDetails tmp = generalUtils.getCustomUserPrincipal();
                tmp.getAccount().setOptions(optionsDTOToOptions.convert(optionsDTO));
                SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(tmp, null, new ArrayList<>()));
                savedNotification.open();
            } catch (ValidationException e) {
                System.out.println(e.getMessage());
            }
        });

        optionsDTOBinder.forField(serverNameComboBox).bind(OptionsDTO::getServer, OptionsDTO::setServer);
        optionsDTOBinder.forField(sendEmailNotificationsCheckBox).bind(OptionsDTO::isReceiveEmailNotifications, OptionsDTO::setReceiveEmailNotifications);
        optionsDTOBinder.readBean(optionsDTO);

        formLayout.addFormItem(regionComboBox, "Region");
        formLayout.addFormItem(serverNameComboBox, "Server Name");
        formLayout.addFormItem(sendEmailNotificationsCheckBox, "Send email notifications");
        formLayout.add(submitFormButton);

        add(titleLabel, formLayout);

    }
}
