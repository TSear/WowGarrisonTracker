package com.trix.wowgarrisontracker.frontEnd.views.settings;

import com.trix.wowgarrisontracker.frontEnd.components.CenteredHeader;
import com.trix.wowgarrisontracker.frontEnd.components.SaveButton;
import com.trix.wowgarrisontracker.frontEnd.components.ServerField;
import com.trix.wowgarrisontracker.frontEnd.interfaces.BinderSave;
import com.trix.wowgarrisontracker.model.Options;
import com.trix.wowgarrisontracker.model.Server;
import com.trix.wowgarrisontracker.services.interfaces.OptionsService;
import com.trix.wowgarrisontracker.services.interfaces.ServerService;
import com.trix.wowgarrisontracker.utils.GeneralUtils;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.PropertyId;

import java.util.List;

public class ServerSettings extends VerticalLayout {

    private final Binder<Options> optionsBinder;
    private final Options options;
    private final FormLayout formLayout;
    private final List<Server> serverList;
    private final SettingsView parentView;
    private final OptionsService optionsService;

    @PropertyId("receiveEmailNotifications")
    private Checkbox sendEmailNotificationsCheckBox;

    @PropertyId("server")
    private ServerField serverField;

    public ServerSettings(ServerService serverService, SettingsView settingsView,
                          OptionsService optionsService) {

        this.optionsService = optionsService;
        this.parentView = settingsView;
        this.options = GeneralUtils.getUserSettings();

        this.optionsBinder = new BeanValidationBinder<>(Options.class);
        this.formLayout = new FormLayout();
        this.serverList = serverService.getServers();


        init();
    }

    private void init() {

        configureFormLayout();

        CenteredHeader centeredHeader = new CenteredHeader("Options");

        serverField = new ServerField(serverList);

        sendEmailNotificationsCheckBox = new Checkbox();

        SaveButton saveButton = new SaveButton(saveLambda());

        optionsBinder.bindInstanceFields(this);
        optionsBinder.readBean(options);

        formLayout.add(serverField);
        formLayout.addFormItem(sendEmailNotificationsCheckBox, "Send email notifications");
        formLayout.add(saveButton);

        add(centeredHeader, formLayout);

    }

    private void configureFormLayout() {
        formLayout.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1));
    }

    private BinderSave saveLambda() {
        return () -> {
            if (optionsBinder.hasChanges()){

                optionsBinder.writeBean(options);
                optionsService.save(options);
                parentView.refreshAll();
                return true;

            }
            return false;
        };
    }


}