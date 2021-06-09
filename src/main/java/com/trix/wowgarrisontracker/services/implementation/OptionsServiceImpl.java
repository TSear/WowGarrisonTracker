package com.trix.wowgarrisontracker.services.implementation;

import com.trix.wowgarrisontracker.exceptions.AccountNotFoundException;
import com.trix.wowgarrisontracker.model.Account;
import com.trix.wowgarrisontracker.model.Options;
import com.trix.wowgarrisontracker.repository.OptionsRepository;
import com.trix.wowgarrisontracker.services.interfaces.AccountService;
import com.trix.wowgarrisontracker.services.interfaces.OptionsService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class OptionsServiceImpl implements OptionsService {


    private final AccountService accountService;
    private final OptionsRepository optionsRepository;

    public OptionsServiceImpl(AccountService accountService,
                              OptionsRepository optionsRepository) {
        this.optionsRepository = optionsRepository;
        this.accountService = accountService;
    }


    @Override
    public boolean updateOptions(Options options, Long accountId) throws AccountNotFoundException {

        Account account = accountService.findById(accountId);

        if (account == null)
            throw new AccountNotFoundException();

        options.setAccount(account);

        return save(options);
    }

    @Override
    public boolean save(Options options) {
        if(options != null && options.getAccount() != null) {
            optionsRepository.save(options);
            return true;
        }
        return false;
    }
}
