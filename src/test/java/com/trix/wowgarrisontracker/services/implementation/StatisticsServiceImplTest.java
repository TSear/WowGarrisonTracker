package com.trix.wowgarrisontracker.services.implementation;

import com.trix.wowgarrisontracker.model.Account;
import com.trix.wowgarrisontracker.model.AccountCharacter;
import com.trix.wowgarrisontracker.model.Entry;
import com.trix.wowgarrisontracker.services.interfaces.AccountCharacterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class StatisticsServiceImplTest {


    Account account;
    AccountCharacterService accountCharacterService;


    @BeforeEach
    void setUp() {
        account = new Account();
        account.setId(1L);

        AccountCharacter accountCharacter = new AccountCharacter();
        accountCharacter.setAccount(account);
        accountCharacter.setCharacterName("Character 1");

        Entry entry1 = new Entry();
        entry1.setAccountCharacter(accountCharacter);
        entry1.setGarrisonResources(100);
        entry1.setWarPaint(100);

        Entry entry2 = new Entry();
        entry2.setAccountCharacter(accountCharacter);
        entry2.setGarrisonResources(200);
        entry2.setWarPaint(200);

        accountCharacter.addNewEntry(entry1, entry2);

        AccountCharacter accountCharacter1 = new AccountCharacter();
        accountCharacter1.setAccount(account);
        accountCharacter1.setCharacterName("Character 1");

        Entry entry3 = new Entry();
        entry3.setAccountCharacter(accountCharacter);
        entry3.setGarrisonResources(300);
        entry3.setWarPaint(300);

        Entry entry4 = new Entry();
        entry4.setAccountCharacter(accountCharacter);
        entry4.setGarrisonResources(400);
        entry4.setWarPaint(400);

        accountCharacter1.addNewEntry(entry4, entry3);

        account.setAccountCharacters(new HashSet<AccountCharacter>(Arrays.asList(accountCharacter, accountCharacter1)));

    }

    @Test
    void getTotalGarrisonResources() {
        //given
        Long correctValue = 1000L;

        //when
        Long totalSum = account.getTotalGarrisonResources();

        //then
        assertEquals(correctValue, totalSum);
    }

    @Test
    void getTotalWarPaint() {
        //given
        Long correctValue = 1000L;

        //when
        Long totalSum = account.getTotalWarPaint();

        //then
        assertEquals(correctValue, totalSum);
    }

    @Test
    void getAverageWarPaintPerDay() {
    }

    @Test
    void getAverageGarrisonResourcesPerDay() {
    }

    @Test
    void getAmountOfEntries() {
    }

    @Test
    void getAmountOfDays() {
    }

    @Test
    void getAllStatistics() {
    }
}