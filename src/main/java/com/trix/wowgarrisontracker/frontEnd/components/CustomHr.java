package com.trix.wowgarrisontracker.frontEnd.components;

import com.trix.wowgarrisontracker.frontEnd.LayoutVariables;
import com.vaadin.flow.component.html.Hr;

public class CustomHr extends Hr {

    public CustomHr() {
       setClassName(LayoutVariables.CONTRAST);
       addClassName(LayoutVariables.MARGIN_BIG);
    }
}
