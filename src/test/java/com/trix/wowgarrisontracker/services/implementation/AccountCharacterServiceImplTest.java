package com.trix.wowgarrisontracker.services.implementation;

import com.trix.wowgarrisontracker.TestUtils;
import com.trix.wowgarrisontracker.converters.AccountCharacterPojoToAccountCharacter;
import com.trix.wowgarrisontracker.converters.AccountCharacterToAccountCharacterPojo;
import com.trix.wowgarrisontracker.model.Account;
import com.trix.wowgarrisontracker.model.AccountCharacter;
import com.trix.wowgarrisontracker.model.Entry;
import com.trix.wowgarrisontracker.pojos.AccountCharacterPojo;
import com.trix.wowgarrisontracker.repository.AccountCharacterRepository;
import com.trix.wowgarrisontracker.services.interfaces.EntryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.trix.wowgarrisontracker.TestUtils.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountCharacterServiceImplTest {

    @Mock
    AccountCharacterToAccountCharacterPojo accountCharacterToAccountCharacterPojo;

    @Mock
    AccountCharacterPojoToAccountCharacter accountCharacterPojoToAccountCharacter;

    @Mock
    AccountCharacterRepository accountCharacterRepository;

    @Mock
    EntryService entryService;

    @InjectMocks
    AccountCharacterServiceImpl accountCharacterService;

    Account account;

    List<AccountCharacter> accountCharacters;

    @BeforeEach
    void setUp() {

        account = new Account();
        account.setId(1L);

        accountCharacters = Arrays.asList(createAccountCharacter("Character 1", 1L, account),
                createAccountCharacter("Character 2", 2L, account),
                createAccountCharacter("Character 3", 3L, account));
        account.getAccountCharacters().addAll(accountCharacters);
    }

    @Test
    void findById_found() {
        //given

        //when
        when(accountCharacterRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(accountCharacters.get(0)));

        //then
        assertEquals(accountCharacters.get(0), accountCharacterService.findById(0L));
    }

    @Test
    void findById_notFound() {
        //given

        //when
        when(accountCharacterRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        //then
        assertNotEquals(accountCharacters.get(0), accountCharacterService.findById(0L));
        assertNull(accountCharacterService.findById(0L));
    }

    @Test
    void addNewEntryToAccountCharacter_success() {

        //given
        AccountCharacter accountCharacter = accountCharacters.get(0);
        Entry entry = TestUtils.generateEntryNoCharacter(100, 200);
        entry.setAccountCharacter(accountCharacter);

        //when
        when(entryService.save(Mockito.any(Entry.class))).thenReturn(new Entry());
        when(accountCharacterRepository.save(Mockito.any(AccountCharacter.class))).thenReturn(accountCharacter);
        boolean result = accountCharacterService.addNewEntryToAccountCharacter(entry);

        //then
        assertEquals(accountCharacter.getTotalGarrisonResorces(), entry.getGarrisonResources());
        assertEquals(accountCharacter.getTotalWarPaint(), entry.getWarPaint());
        assertTrue(result);
    }

    @Test
    void addNewEntryToAccountCharacter_fail_accountCharacterNull() {

        //given
        AccountCharacter accountCharacter = accountCharacters.get(0);
        Entry entry = TestUtils.generateEntryNoCharacter(100, 200);

        //when
        boolean result = accountCharacterService.addNewEntryToAccountCharacter(entry);

        //then
        assertNotEquals(entry.getGarrisonResources(), accountCharacter.getTotalGarrisonResorces());
        assertNotEquals(entry.getWarPaint(), accountCharacter.getWarPaint());
        assertFalse(result);

    }

    @Test
    void addNewEntryToAccountCharacter_fail_entryNull() {
        //given
        AccountCharacter accountCharacter = accountCharacters.get(0);
        Entry entry = null;

        //when
        boolean result = accountCharacterService.addNewEntryToAccountCharacter(entry);

        //then
        assertNotEquals(1, accountCharacter.getAmountOfEntries());
        assertEquals(0, accountCharacter.getTotalGarrisonResorces());
        assertEquals(0, accountCharacter.getTotalWarPaint());
        assertFalse(result);
    }

    @Test
    void removeEntryFromAccountCharacter_success() {
        //given

        AccountCharacter accountCharacter = accountCharacters.get(0);
        Entry entry = TestUtils.generateEntryWithCharacter(100, 100, accountCharacter);

        //when
        when(accountCharacterRepository.save(Mockito.any(AccountCharacter.class))).thenReturn(accountCharacter);


        //then
        assertEquals(1, accountCharacter.getAmountOfEntries());
        boolean result = accountCharacterService.removeEntryFromAccountCharacter(entry);
        assertEquals(0, accountCharacter.getAmountOfEntries());
        assertEquals(0, accountCharacter.getTotalGarrisonResorces());
        assertEquals(0, accountCharacter.getTotalWarPaint());
        assertTrue(result);
    }

    @Test
    void removeEntryFromAccountCharacter_fail_entryNull() {
        //given
        AccountCharacter accountCharacter = accountCharacters.get(0);
        generateEntryWithCharacter(100, 100, accountCharacter);
        Entry entry2 = generateEntryNoCharacter(100, 100);

        //when
        boolean result = accountCharacterService.removeEntryFromAccountCharacter(entry2);

        //then
        assertEquals(100, accountCharacter.getTotalWarPaint());
        assertEquals(100, accountCharacter.getGarrisonResources());
        assertFalse(result);

    }

    @Test
    void findAllByAccountId() {
        //given

        //when
        when(accountCharacterRepository.findAllByAccountId(Mockito.anyLong())).thenReturn(accountCharacters);

        //then
        assertEquals(accountCharacters.size(), accountCharacterService.findAllByAccountId(1L).size());

    }

    @Test
    void isNameTaken_true() {
        //given
        Long id = 1L;
        String name = accountCharacters.get(2).getCharacterName();

        //when
        when(accountCharacterRepository.findAllByAccountId(Mockito.anyLong())).thenReturn(accountCharacters);

        //then
        assertTrue(accountCharacterService.isNameTaken(id, name));
    }

    @Test
    void isNameTaken_false() {
        //given
        Long id = 1L;
        String name = "NotInList";

        //when
        when(accountCharacterRepository.findAllByAccountId(Mockito.anyLong())).thenReturn(accountCharacters);

        //then
        assertFalse(accountCharacterService.isNameTaken(id, name));
    }

    @Test
    void save_accountCharacterPojo_success() {
        //given
        AccountCharacter accountCharacter = accountCharacters.get(0);
        AccountCharacterPojo accountCharacterPojo = new AccountCharacterPojo();
        accountCharacterPojo.setAccountId(accountCharacter.getId());
        accountCharacterPojo.setCharacterName(accountCharacter.getCharacterName());
        accountCharacterPojo.setAccountId(accountCharacter.getAccount().getId());

        //when
        when(accountCharacterPojoToAccountCharacter.convert(accountCharacterPojo)).thenReturn(accountCharacter);


        //then
        assertTrue(accountCharacterService.save(accountCharacterPojo));
    }

    @Test
    void save_accountCharacterPojoNull() {
        //given

        //when

        //then
        assertFalse(accountCharacterService.save((AccountCharacterPojo) null));
    }

    @Test
    void save_accountCharacter_success() {
        //given
        AccountCharacter accountCharacter = accountCharacters.get(0);

        //when

        //then
        assertTrue(accountCharacterService.save(accountCharacter));
    }

    @Test
    void save_accountCharacter_AccountCharacterNull() {
        //given

        //when

        //then
        assertFalse(accountCharacterService.save((AccountCharacter) null));
    }

}