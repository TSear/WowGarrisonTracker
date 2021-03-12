package com.trix.wowgarrisontracker.frontEnd.fragments;

import com.vaadin.flow.router.Route;

@Route(value = "view2", layout = MainLayout.class)
public class View2 extends AbstractView{
    @Override
    String getViewName() {
       return getClass().getName();
    }
}
