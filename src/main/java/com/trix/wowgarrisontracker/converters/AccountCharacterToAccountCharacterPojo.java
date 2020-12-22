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
public class AccountCharacterToAccountCharacterPojo implements Converter<AccountCharacter, AccountCharacterPojo>{

    private EntryToEntryPojo entryConverter = new EntryToEntryPojo();

    @Override
    public AccountCharacterPojo convert(AccountCharacter source) {
        AccountCharacterPojo accountCharacterPojo = new AccountCharacterPojo();
        accountCharacterPojo.setAccountId(source.getAccountId());
        accountCharacterPojo.setCharacterName(source.getCharacterName());
        accountCharacterPojo.setId(source.getId());

        Set<EntryPojo> entryPojos = new HashSet<>();

        for(Entry tmp : source.getEntries()){
            entryPojos.add(entryConverter.convert(tmp));
        }

        accountCharacterPojo.setEntries(entryPojos);
        return accountCharacterPojo;
    }
    
}
