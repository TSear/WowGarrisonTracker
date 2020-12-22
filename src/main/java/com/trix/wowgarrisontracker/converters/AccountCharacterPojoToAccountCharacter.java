package com.trix.wowgarrisontracker.converters;

import java.util.HashSet;
import java.util.Set;

import com.trix.wowgarrisontracker.model.AccountCharacter;
import com.trix.wowgarrisontracker.model.Entry;
import com.trix.wowgarrisontracker.pojos.AccountCharacterPojo;
import com.trix.wowgarrisontracker.pojos.EntryPojo;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class AccountCharacterPojoToAccountCharacter implements Converter<AccountCharacterPojo, AccountCharacter>{

    @Override
    public AccountCharacter convert(AccountCharacterPojo source) {
        
        AccountCharacter accountCharacter = new AccountCharacter();
        accountCharacter.setAccountId(source.getAccountId());
        accountCharacter.setCharacterName(source.getCharacterName());
        accountCharacter.setId(source.getId());
        Set<Entry> entries = new HashSet<>();

        for(EntryPojo tmp : source.getEntries()){

        }

        accountCharacter.setEntries(entries);

        return accountCharacter;
    }
    
}
