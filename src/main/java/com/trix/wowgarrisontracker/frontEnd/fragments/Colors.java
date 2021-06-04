package com.trix.wowgarrisontracker.frontEnd.fragments;

public enum Colors {

    DETAILS_COLOR("#EB5E28"),
    PRIMARY_BACKGROUND_COLOR("#252422"),
    SECONDARY_BACKGROUND_COLOR("#403D39"),
    PRIMARY_FONT_COLOR("#CCC5B9");

    private final String color;

    Colors(String color) {
        this.color = color;
    }

    public String getColor() {
        return this.color;
    }
}
