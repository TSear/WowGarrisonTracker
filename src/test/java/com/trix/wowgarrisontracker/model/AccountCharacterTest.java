package com.trix.wowgarrisontracker.model;

import com.trix.wowgarrisontracker.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AccountCharacterTest {

    AccountCharacter accountCharacter;
    Entry testEntry;

    @BeforeEach
    void setUp() {
        accountCharacter = new AccountCharacter();

        Entry entry1 = TestUtils.entryGenerator(200, 300, accountCharacter);
        Entry entry2 = TestUtils.entryGenerator(200, 300, accountCharacter);
        Entry entry3 = TestUtils.entryGenerator(200, 300, accountCharacter);

        entry1.setId(0L);
        entry2.setId(1L);
        entry3.setId(2L);

        accountCharacter.addNewEntry(entry1,entry2,entry3);
        testEntry = entry1;
    }

    @Test
    void containsEntry_true() {
        //given

        //when
        boolean contains = accountCharacter.containsEntry(testEntry);
        //then
        assertTrue(contains);
    }

    @Test
    void containsEntry_false() {
        //given

        //when
        boolean contains = accountCharacter.containsEntry(new Entry());
        //then
        assertFalse(contains);
    }

    @Test
    void removeEntry_success() {
        //given
        int size = accountCharacter.getAmountOfEntries();

        //when
        boolean removed = accountCharacter.removeEntry(testEntry);
        //then
        assertEquals(size-1, accountCharacter.getAmountOfEntries());
        assertTrue(removed);
    }

    @Test
    void removeEntry_fail() {
        //given
        int size = accountCharacter.getAmountOfEntries();
        //when
        boolean removed = accountCharacter.removeEntry(new Entry());

        //then
        assertEquals(size, accountCharacter.getAmountOfEntries());
        assertFalse(removed);
    }
}