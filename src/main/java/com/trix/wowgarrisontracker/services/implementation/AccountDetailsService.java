package com.trix.wowgarrisontracker.services.implementation;

import com.trix.wowgarrisontracker.model.Account;
import com.trix.wowgarrisontracker.model.CustomUserDetails;
import com.trix.wowgarrisontracker.services.interfaces.AccountService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AccountDetailsService implements UserDetailsService {

    private final AccountService accountService;

    public AccountDetailsService(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Account account = accountService.findAccountByLogin(username);

        if (account == null) {
            throw new UsernameNotFoundException("User was not found");
        }
        return new CustomUserDetails(account);
    }


}
