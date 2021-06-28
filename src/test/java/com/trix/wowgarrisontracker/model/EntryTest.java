package com.trix.wowgarrisontracker.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EntryTest {

    Entry entry;

    @BeforeEach
    public void init() {
        entry = new Entry();
        entry.setGarrisonResources(100);
        entry.setWarPaint(100);
    }


}