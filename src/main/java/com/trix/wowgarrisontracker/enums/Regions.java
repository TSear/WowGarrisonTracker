package com.trix.wowgarrisontracker.enums;

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
