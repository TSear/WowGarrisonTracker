package com.trix.wowgarrisontracker.frontEnd.components;

import com.trix.wowgarrisontracker.frontEnd.LayoutVariables;
import com.trix.wowgarrisontracker.frontEnd.interfaces.BinderSave;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.data.binder.ValidationException;

public class SaveButton extends Button {

    public SaveButton(BinderSave binderSave) {
        setText("Save");
        setWidthFull();
        setClassName(LayoutVariables.MARGIN_BIG);

        addClickListener(event -> {
            try {
                if (binderSave.save()) {
                    Notification notification = new Notification("Settings saved", 5000);
                    notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                    notification.open();
                }else{
                    Notification notification = new Notification("Nothing to save or error occurred", 5000);
                    notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                    notification.open();
                }
                ;
            } catch (ValidationException e) {
                e.printStackTrace();
            }
        });
    }
}
