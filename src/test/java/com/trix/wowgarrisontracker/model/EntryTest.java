package com.trix.wowgarrisontracker.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

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
    void getUpdatedAccountCharacter_ThrowExceptionWhenAccountCharacterIsNull() {
        //given
        entry.setAccountCharacter(null);

        //when
        Exception exception1 = assertThrows(RuntimeException.class, () -> entry.getUpdatedAccountCharacterGarrisonResources());
        Exception exception2 = assertThrows(RuntimeException.class, () -> entry.getUpdatedAccountCharacterWarPaint());
        //then

    }

    @Test
    void getUpdatedAccountCharacter_DoesntThrowExceptionWhenAccountCharacterIsNotNull() {
        //given
        entry.setAccountCharacter(new AccountCharacter());

        //when
        assertDoesNotThrow(() -> entry.getUpdatedAccountCharacterGarrisonResources());
        assertDoesNotThrow(() -> entry.getUpdatedAccountCharacterWarPaint());
        //then
    }

    @ParameterizedTest
    @CsvSource(value = {"100,200", "100,300", "200,400"})
    void getUpdateAccountCharacter_CorrectAmountReturned(int resources, int characterResources) {
        AccountCharacter accountCharacter = new AccountCharacter();
        accountCharacter.setGarrisonResources((long) characterResources);
        accountCharacter.setWarPaint((long) characterResources);
        entry.setAccountCharacter(accountCharacter);
        entry.setGarrisonResources(resources);
        entry.setWarPaint(resources);

        assertEquals(resources + characterResources, entry.getUpdatedAccountCharacterGarrisonResources());
        assertEquals(resources + characterResources, entry.getUpdatedAccountCharacterWarPaint());
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