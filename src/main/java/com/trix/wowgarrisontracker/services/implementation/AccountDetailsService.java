package com.trix.wowgarrisontracker.services.implementation;

import com.trix.wowgarrisontracker.model.CustomUserDetails;
import com.trix.wowgarrisontracker.services.interfaces.AccountService;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
@Service
public class AccountDetailsService implements UserDetailsService {

    private AccountService accountService;

    public AccountDetailsService(AccountService acccountService) {
        this.accountService = acccountService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new CustomUserDetails(accountService.findUserByUsername(username));
    }

    

}
