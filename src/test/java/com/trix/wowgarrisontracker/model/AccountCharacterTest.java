package com.trix.wowgarrisontracker.model;

import com.trix.wowgarrisontracker.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class AccountCharacterTest {

    AccountCharacter accountCharacter;
    Entry testEntry;

    @BeforeEach
    void setUp() {
        accountCharacter = new AccountCharacter();

        Entry entry1 = TestUtils.generateEntryWithCharacter(200, 300, accountCharacter);
        Entry entry2 = TestUtils.generateEntryWithCharacter(200, 300, accountCharacter);
        Entry entry3 = TestUtils.generateEntryWithCharacter(200, 300, accountCharacter);

        entry1.setId(0L);
        entry2.setId(1L);
        entry3.setId(2L);

        accountCharacter.getEntries().addAll(Arrays.asList(entry1,entry2,entry3));
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

}