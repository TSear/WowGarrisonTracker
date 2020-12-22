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
public class AccountPojoToAccount implements Converter<AccountPojo, Account>{

    @Override
    public Account convert(AccountPojo source) {
        Account account = new Account();
        //account.setId(source.getId());
        account.setLogin(source.getLogin());
        account.setPassword(source.getPassword());
        
        Set<AccountCharacter> accountCharacterPojos = new HashSet<>();
        
        for(AccountCharacterPojo tmp : source.getAccountCharacters()){

        }

        account.setAccountCharacters(accountCharacterPojos);

        return account;
    }
    
}
