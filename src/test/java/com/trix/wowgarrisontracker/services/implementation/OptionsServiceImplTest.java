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
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
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
    }

    @Test
    void updateOptions_updatedSuccessfully() {

        //given
        options.setAccount(account);

        //when
        when(repository.save(options)).thenReturn(options);

        //then
        assertTrue(optionsService.updateOptions(options));
    }
//
//    @Test
//    void updateOptions_updateFailed() {
//
//        //given
//        options.setAccount(account);
//
//        //when
//
//        //then
//        assertFalse(optionsService.updateOptions(options));
//    }

    @Test()
    void changeAccountAndUpdate_Successful() {
        //given

        //when
        when(repository.save(options)).thenReturn(options);
        when(accountService.findById(Mockito.anyLong())).thenReturn(account);
        options.setAccount(account);

        //then
        try {
            assertTrue(optionsService.updateOptions(options, 1L));
        } catch (Exception e) {

        }
    }

    @Test()
    void changeAccountAndUpdate_Fail() {
        //given

        //when
        when(accountService.findById(Mockito.anyLong())).thenReturn(null);

        //then
        try {
            optionsService.updateOptions(options, 1L);
            fail("Exception was not thrown");
        } catch (AccountNotFoundException e) {
        }

    }
}

