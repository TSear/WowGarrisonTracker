package com.trix.wowgarrisontracker.frontEnd.interfaces;

import com.vaadin.flow.data.binder.ValidationException;

public interface BinderSave {

    boolean save() throws ValidationException;

}

