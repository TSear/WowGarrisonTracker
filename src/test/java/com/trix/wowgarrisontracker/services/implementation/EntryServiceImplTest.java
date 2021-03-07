package com.trix.wowgarrisontracker.services.implementation;

import com.sun.source.tree.ModuleTree;
import com.trix.wowgarrisontracker.converters.EntryPojoToEntry;
import com.trix.wowgarrisontracker.converters.EntryToEntryPojo;
import com.trix.wowgarrisontracker.model.Account;
import com.trix.wowgarrisontracker.model.AccountCharacter;
import com.trix.wowgarrisontracker.model.Entry;
import com.trix.wowgarrisontracker.repository.EntryRepository;
import com.trix.wowgarrisontracker.services.interfaces.AccountCharacterService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EntryServiceImplTest {

    @Mock
    EntryRepository entryRepository;
    @Mock
    EntryPojoToEntry entryPojoToEntry;
    @Mock
    AccountCharacterServiceImpl accountCharacterService;
    EntryToEntryPojo entryToEntryPojo;
    EntryServiceImpl entryServiceImpl;
    List<AccountCharacter> listOfAccountCharacters;

    @BeforeEach
    void setUp() {
        entryToEntryPojo = new EntryToEntryPojo();
        entryServiceImpl = new EntryServiceImpl(entryRepository, entryPojoToEntry, accountCharacterService, entryToEntryPojo);


        Account account = new Account();
        account.setId(1l);

        Entry entry1 = new Entry();
        entry1.setWarPaint(100);
        entry1.setGarrisonResources(100);
        entry1.setId(1l);
        Entry entry2 = new Entry();
        entry2.setWarPaint(100);
        entry2.setGarrisonResources(100);
        entry2.setId(2l);

        listOfAccountCharacters = new ArrayList<>();
        AccountCharacter character1 = new AccountCharacter();
        character1.setAccount(account);
        character1.setId(1l);
        character1.setCharacterName("character1");
        entry1.setAccountCharacter(character1);
        character1.setEntries(Collections.singleton(entry1));

        AccountCharacter character2 = new AccountCharacter();
        character2.setAccount(account);
        character2.setId(1l);
        character2.setCharacterName("character2");
        entry2.setAccountCharacter(character2);
        character2.setEntries(Collections.singleton(entry2));

        AccountCharacter character3 = new AccountCharacter();
        character3.setAccount(account);
        character3.setId(1l);
        character3.setCharacterName("character3");

        listOfAccountCharacters.addAll(Arrays.asList(character1,character2,character3));
    }

    @Test
    void accountEntriesConvertedToPojoList() {
        //given


        //when
        when(accountCharacterService.findAllByAccountId(Mockito.anyLong())).thenReturn(listOfAccountCharacters);

        //then
        assertEquals(2, entryServiceImpl.accountEntriesConvertedToPojoList(anyLong()).size());
    }
}