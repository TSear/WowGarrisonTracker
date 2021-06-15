package com.trix.wowgarrisontracker.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum Regions {

    ALL("All"), EU("EU"), US("US");

    String value;

    Regions(String text) {
        this.value = text;
    }

    public String getValue() {
        return this.value;
    }


}
