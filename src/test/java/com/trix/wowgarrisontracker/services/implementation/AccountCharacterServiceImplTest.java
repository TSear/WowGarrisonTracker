package com.trix.wowgarrisontracker.services.implementation;

import com.trix.wowgarrisontracker.converters.AccountCharacterToAccountCharacterPojo;
import com.trix.wowgarrisontracker.model.Account;
import com.trix.wowgarrisontracker.model.AccountCharacter;
import com.trix.wowgarrisontracker.pojos.AccountCharacterForm;
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

import static com.trix.wowgarrisontracker.TestUtils.createAccountCharacter;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountCharacterServiceImplTest {

    @Mock
    AccountCharacterToAccountCharacterPojo accountCharacterToAccountCharacterPojo;


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
        AccountCharacterForm accountCharacterPojo = new AccountCharacterForm();
        accountCharacterPojo.setAccountId(accountCharacter.getId());
        accountCharacterPojo.setAccountCharacterName(accountCharacter.getCharacterName());
        accountCharacterPojo.setAccountId(accountCharacter.getAccount().getId());

        //when

        //then
        assertTrue(accountCharacterService.save(accountCharacterPojo));
    }

    @Test
    void save_accountCharacterPojoNull() {
        //given

        //when

        //then
        assertFalse(accountCharacterService.save((AccountCharacterForm) null));
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