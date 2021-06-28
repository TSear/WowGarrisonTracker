package com.trix.wowgarrisontracker.services.implementation;

import com.trix.wowgarrisontracker.model.Account;
import com.trix.wowgarrisontracker.pojos.RegisterPojo;
import com.trix.wowgarrisontracker.repository.AccountRepository;
import com.trix.wowgarrisontracker.services.interfaces.OptionsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServicesImplTest {


    @Mock
    AccountRepository accountRepository;

    @Mock
    OptionsService optionsService;

    BCryptPasswordEncoder passwordEncoder;

    AccountServicesImpl accountServices;

    RegisterPojo registerPojo;

    Account account;

    String verificationUrl = "";

    @BeforeEach
    void setUp() {

        passwordEncoder = new BCryptPasswordEncoder();

        accountServices = new AccountServicesImpl(accountRepository, passwordEncoder,null);

        registerPojo = new RegisterPojo();
        registerPojo.setLogin("login1");
        registerPojo.setPassword("password1");
        registerPojo.setServer(null);

        account = new Account();
        account.setLogin("Login1");
        account.setPassword(passwordEncoder.encode("Password1"));
        account.setId(1L);
    }

    @Test
    void createAccount_success() {
        //given
        //when
        when(accountRepository.existsByLogin(anyString())).thenReturn(false);
        //then
        assertTrue(accountServices.register(registerPojo, verificationUrl));
    }

    @Test
    void createAccount_fail() {
        //given
        //when
        when(accountRepository.existsByLogin(anyString())).thenReturn(true);
        //then
        assertFalse(accountServices.register(registerPojo, verificationUrl));
        assertFalse(accountServices.register(null, verificationUrl));
    }

    @Test
    void isLoginTaken_true() {
        //given
        //when
        when(accountRepository.existsByLogin(anyString())).thenReturn(true);
        //then
        assertTrue(accountServices.isLoginTaken(""));
    }

    @Test
    void isLoginTaken_false() {
        //given
        //when
        when(accountRepository.existsByLogin(anyString())).thenReturn(false);
        //then
        assertFalse(accountServices.isLoginTaken(""));
        assertFalse(accountServices.isLoginTaken(null));
    }

    @Test
    void findAccountByLogin_success() {
        //given


        //when
        when(accountRepository.findByLogin(anyString())).thenReturn(Optional.of(account));

        //then
        assertNotNull(accountServices.findAccountByLogin(""));
    }

    @Test
    void findAccountByLogin_fail() {
        //given

        //when
        when(accountRepository.findByLogin(anyString())).thenReturn(Optional.empty());

        //then
        assertNull(accountServices.findAccountByLogin(" "));
    }

    @Test
    void attemptToLogIn_success() {
        //given

        //when
        when(accountRepository.findByLogin(anyString())).thenReturn(Optional.of(account));

        //then
        assertNotNull(accountServices.attemptToLogIn("Login1", "Password1"));
    }

    @Test
    void attemptToLogIn_fail_wrongCredentials() {
        //given

        //when
        when(accountRepository.findByLogin(anyString())).thenReturn(Optional.of(account));

        //then
        assertNull(accountServices.attemptToLogIn("Login1", "wrong password"));
    }

    @Test
    void attemptToLogIn_fail_AccountNotFound() {
        //given

        //when
        when(accountRepository.findByLogin(anyString())).thenReturn(Optional.empty());

        //then
        assertNull(accountServices.attemptToLogIn("asd", "a"));
    }

    @Test
    void findById_success() {
        //given

        //when
        when(accountRepository.findById(anyLong())).thenReturn(Optional.of(account));

        //then
        assertNotNull(accountServices.findById(1L));
    }

    @Test
    void findById_fail() {
        //given

        //when
        when(accountRepository.findById(anyLong())).thenReturn(Optional.empty());

        //then
        assertNull(accountServices.findById(1L));
    }
}