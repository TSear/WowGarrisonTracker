package com.trix.wowgarrisontracker.frontEnd.fragments;

import com.vaadin.flow.router.Route;

@Route(value = "view3", layout = MainLayout.class)
public class View3 extends AbstractView{
    @Override
    String getViewName() {
       return getClass().getName();
    }
}
