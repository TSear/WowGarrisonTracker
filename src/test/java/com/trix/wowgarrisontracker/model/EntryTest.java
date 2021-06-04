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

    @Test
    void changeGarrisonResourcesToNegative() {
        //when
        entry.setGarrisonResourcesToNegative();

        //Then
        assertEquals(-100, entry.getGarrisonResources());
        assertNotEquals(100, entry.getGarrisonResources());
        assertTrue(entry.getGarrisonResources() < 0);
    }

    @Test
    void changeWarPaintToNegative() {
        //when
        entry.setWarPaintToNegative();

        //Then
        assertEquals(-100, entry.getWarPaint());
        assertNotEquals(100, entry.getWarPaint());
        assertTrue(entry.getWarPaint() < 0);
    }
}