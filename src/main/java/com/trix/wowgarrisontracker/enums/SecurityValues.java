package com.trix.wowgarrisontracker.enums;

public enum SecurityValues {

    AUTHRORIZATION("Authorization"), ACCOUNT_ID("accountId"), BEARER("Bearer_");

    private final String name;

    SecurityValues(String name) {
        this.name = name;
    }

    public String toString() {
        return this.name;
    }

}
