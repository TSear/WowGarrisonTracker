package com.trix.wowgarrisontracker.services.implementation;

import com.trix.wowgarrisontracker.converters.AccountCharacterToAccountCharacterPojo;
import com.trix.wowgarrisontracker.converters.EntryToEntryPojo;
import com.trix.wowgarrisontracker.model.Account;
import com.trix.wowgarrisontracker.model.AccountCharacter;
import com.trix.wowgarrisontracker.model.Entry;
import com.trix.wowgarrisontracker.pojos.AccCharacterResourcesPojo;
import com.trix.wowgarrisontracker.pojos.AccountCharacterPojo;
import com.trix.wowgarrisontracker.repository.AccountCharacterRepository;
import com.trix.wowgarrisontracker.repository.EntryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountCharacterServiceImplTest {

    @Mock
    EntryToEntryPojo entryToEntryPojo;
    @InjectMocks
    AccountCharacterToAccountCharacterPojo accountCharacterToAccountCharacterPojo;
    @Mock
    EntryRepository entryRepository;
    @Mock
    AccountCharacterRepository accountCharacterRepository;

    AccountCharacterServiceImpl accountCharacterService;

    Account account;

    @BeforeEach
    void setUp() {
        accountCharacterService = new AccountCharacterServiceImpl(accountCharacterRepository,
                entryRepository,
                null,
                accountCharacterToAccountCharacterPojo);

        account = new Account();
        account.setId(1l);
    }

    @Test
    void listOfResources() {
        //given

        AccountCharacter accountCharacter = new AccountCharacter();
        accountCharacter.setCharacterName("test1");
        accountCharacter.setAccount(account);
        accountCharacter.setId(1l);

        Entry entry = new Entry();
        entry.setId(1l);
        entry.setAccountCharacter(accountCharacter);
        entry.setGarrisonResources(100);
        entry.setWarPaint(100);

        accountCharacter.setEntries(Collections.singleton(entry));

        //when
        when(accountCharacterRepository.findAllByAccountId(1l))
                .thenReturn(Collections.singletonList(accountCharacter));
        when(entryRepository.getGarrisonResourcesByCharacterId(1l)).thenReturn((long) entry.getGarrisonResources());
        when(entryRepository.getWarPaintByCharacterId(1l)).thenReturn((long) entry.getWarPaint());
        List<AccCharacterResourcesPojo> accCharacterResourcesPojoList = accountCharacterService.listOfResources(1l);

        //then
        assertEquals(1, accountCharacterService.listOfAccountCharacters(1l).size());
        assertEquals(entry.getGarrisonResources(), accCharacterResourcesPojoList.get(0).getGarrisonResources());
        assertEquals(entry.getWarPaint(), accCharacterResourcesPojoList.get(0).getWarPaint());
    }

    @Test
    void getListOfAccountCharactersConvertedToPojo_correctSizeAndData() {

        //given
        AccountCharacter accountCharacter1 = new AccountCharacter();
        accountCharacter1.setId(1l);
        accountCharacter1.setCharacterName("test");
        accountCharacter1.setAccount(account);

        AccountCharacter accountCharacter2 = new AccountCharacter();
        accountCharacter2.setId(2l);
        accountCharacter2.setCharacterName("test2");
        accountCharacter2.setAccount(account);

        List<AccountCharacter> characters = new ArrayList<>();
        characters.addAll(Arrays.asList(accountCharacter1, accountCharacter2, accountCharacter1));

        //when
        when(accountCharacterRepository.findAllByAccountId(Mockito.anyLong())).thenReturn(characters);
        //then

        List<AccountCharacterPojo> accountCharacterPojoList = accountCharacterService.getListOfAccountCharactersConvertedToPojo(0l);
        assertEquals(characters.size(), accountCharacterPojoList.size());
        assertEquals(accountCharacter1.getCharacterName(), accountCharacterPojoList.get(0).getCharacterName());
        assertEquals(accountCharacter2.getCharacterName(), accountCharacterPojoList.get(1).getCharacterName());
    }
}