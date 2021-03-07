package com.trix.wowgarrisontracker.converters;

import com.trix.wowgarrisontracker.model.AccountCharacter;
import com.trix.wowgarrisontracker.pojos.AccountCharacterPojo;
import com.trix.wowgarrisontracker.pojos.EntryPojo;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class AccountCharacterToAccountCharacterPojo implements Converter<AccountCharacter, AccountCharacterPojo> {

    private EntryToEntryPojo entryConverter;

    /**
     * @param entryConverter
     */
    public AccountCharacterToAccountCharacterPojo(EntryToEntryPojo entryConverter) {
        super();
        this.entryConverter = entryConverter;
    }

    @Override
    public AccountCharacterPojo convert(AccountCharacter source) {
        AccountCharacterPojo accountCharacterPojo = new AccountCharacterPojo();
        accountCharacterPojo.setAccountId(source.getAccount().getId());
        accountCharacterPojo.setCharacterName(source.getCharacterName());
        accountCharacterPojo.setId(source.getId());
        accountCharacterPojo.setGarrisonResources(source.getGarrisonResources());
        accountCharacterPojo.setWarPaint(source.getWarPaint());
        Set<EntryPojo> entryPojos = new HashSet<>();
        accountCharacterPojo.setEntries(entryPojos);
        return accountCharacterPojo;
    }

}
