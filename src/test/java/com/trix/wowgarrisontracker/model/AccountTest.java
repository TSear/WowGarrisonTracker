package com.trix.wowgarrisontracker.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith(MockitoExtension.class)
public class AccountTest {

    Account testingAccount;

    @BeforeEach
    void setUp() {

        testingAccount = new Account();
        testingAccount.setId(0L);

        AccountCharacter accountCharacter = new AccountCharacter();
        accountCharacter.setCharacterName("testingCharacter1");
        accountCharacter.setAccount(testingAccount);

        accountCharacter.addNewEntry(entryCreator(100, 100, accountCharacter),
                entryCreator(200, 200, accountCharacter));

        testingAccount.getAccountCharacters().add(accountCharacter);

    }

    public Entry entryCreator(Integer garrisonResources, Integer warPaint, AccountCharacter accountCharacter) {

        Entry entry = new Entry();
        entry.setWarPaint(warPaint);
        entry.setGarrisonResources(garrisonResources);
        entry.setAccountCharacter(accountCharacter);
        return entry;
    }

    @Test
    public void getTotalGarrisonResources_CorrectAmount() {
        //given

        //when
        Long garrisonResources = testingAccount.getTotalGarrisonResources();
        //then
        assertEquals(300, (long) garrisonResources);
    }

    @Test
    public void getTotalWarPaint_CorrectAmount() {
        //given

        //when
        Long warPaint = testingAccount.getTotalWarPaint();

        //then
        assertEquals(300, (long) warPaint);

    }

}
