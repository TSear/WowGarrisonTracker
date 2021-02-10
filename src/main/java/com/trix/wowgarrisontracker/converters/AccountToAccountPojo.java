package com.trix.wowgarrisontracker.converters;

import java.util.HashSet;
import java.util.Set;

import com.trix.wowgarrisontracker.model.Account;
import com.trix.wowgarrisontracker.model.AccountCharacter;
import com.trix.wowgarrisontracker.pojos.AccountCharacterPojo;
import com.trix.wowgarrisontracker.pojos.AccountPojo;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class AccountToAccountPojo implements Converter<Account, AccountPojo>{

    private AccountCharacterToAccountCharacterPojo accountCharacterConverter = new AccountCharacterToAccountCharacterPojo();

    @Override
    public AccountPojo convert(Account source) {
        AccountPojo accountPojo = new AccountPojo();

        accountPojo.setId(source.getId());
        accountPojo.setLogin(source.getLogin());
        accountPojo.setPassword(source.getPassword());
        
        Set<AccountCharacterPojo> accountCharacterPojos = new HashSet<>();
        
//        for(AccountCharacter tmp : source.getAccountCharacters()){
//            accountCharacterPojos.add(accountCharacterConverter.convert(tmp));
//        }

        accountPojo.setAccountCharacters(accountCharacterPojos);

        return accountPojo;
    }
    
}
