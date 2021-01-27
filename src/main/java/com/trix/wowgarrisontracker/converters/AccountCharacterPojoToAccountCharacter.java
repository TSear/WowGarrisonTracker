package com.trix.wowgarrisontracker.converters;

import java.util.HashSet;
import java.util.Set;

import com.trix.wowgarrisontracker.model.AccountCharacter;
import com.trix.wowgarrisontracker.model.Entry;
import com.trix.wowgarrisontracker.pojos.AccountCharacterPojo;
import com.trix.wowgarrisontracker.pojos.EntryPojo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class AccountCharacterPojoToAccountCharacter implements Converter<AccountCharacterPojo, AccountCharacter> {

    @Autowired
    private EntryPojoToEntry entryConverter;

    @Override
    public AccountCharacter convert(AccountCharacterPojo source) {

        AccountCharacter accountCharacter = new AccountCharacter();
        accountCharacter.setAccountId(source.getAccountId());
        accountCharacter.setCharacterName(source.getCharacterName());
        accountCharacter.setId(source.getId());
        Set<Entry> entries = new HashSet<>();
        if (source.getEntries() != null && source.getEntries().isEmpty()) {
            for (EntryPojo tmp : source.getEntries()) {
                entries.add(entryConverter.convert(tmp));
            }
        }

        accountCharacter.setEntries(entries);

        return accountCharacter;
    }

}
