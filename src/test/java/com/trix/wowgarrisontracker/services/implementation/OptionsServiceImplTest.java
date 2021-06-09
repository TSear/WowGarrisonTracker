package com.trix.wowgarrisontracker.services.implementation;

import com.trix.wowgarrisontracker.exceptions.AccountNotFoundException;
import com.trix.wowgarrisontracker.model.Account;
import com.trix.wowgarrisontracker.model.Options;
import com.trix.wowgarrisontracker.repository.OptionsRepository;
import com.trix.wowgarrisontracker.services.interfaces.AccountService;
import org.junit.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OptionsServiceImplTest {

    @Rule
    ExpectedException expectedException = ExpectedException.none();

    @Mock
    OptionsRepository repository;

    @Mock
    AccountService accountService;

    @InjectMocks
    OptionsServiceImpl optionsService;

    Options options;

    Account account;

    @BeforeEach
    void setUp() {
        options = new Options();
        account = new Account();
        account.setId(1L);
        options.setAccount(account);
    }

    @Test
    void updateOptions_success() {
        //given
        //when
        when(accountService.findById(anyLong())).thenReturn(account);

        //then
        try {
            assertTrue(optionsService.updateOptions(options, account.getId()));
        } catch (AccountNotFoundException e) {
            fail();
        }
    }

    @Test
    void updateOptions_fail() {
        //given
        //when
        when(accountService.findById(anyLong())).thenReturn(null);

        //then
        assertThrows(AccountNotFoundException.class, () -> optionsService.updateOptions(options, 1L));
    }

    @Test
    void save_success() {
        //given
        //when
        //then
        assertTrue(optionsService.save(options));
    }

    @Test
    void save_fail() {
        //given
        //when
        //then
        assertFalse(optionsService.save(null));
    }
}

