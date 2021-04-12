package com.trix.wowgarrisontracker.converters;

import com.trix.wowgarrisontracker.model.Account;
import com.trix.wowgarrisontracker.model.Options;
import com.trix.wowgarrisontracker.pojos.OptionsDTO;
import com.trix.wowgarrisontracker.services.interfaces.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

public class OptionsDTOToOptions implements Converter<OptionsDTO, Options> {

    private AccountService accountService;

    @Autowired
    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }


    @Override
    public Options convert(OptionsDTO optionsDTO) {

        Options options = new Options();
        Account account = accountService.findById(optionsDTO.getAccountId());

        options.setId(optionsDTO.getId());
        options.setServer(optionsDTO.getServer());
        options.setReceiveEmailNotifications(optionsDTO.isReceiveEmailNotifications());
        options.setAccount(account);

        return options;
    }
}
