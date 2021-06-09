package com.trix.wowgarrisontracker.converters;

import com.trix.wowgarrisontracker.pojos.Entry;
import com.trix.wowgarrisontracker.repository.AccountCharacterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class EntryPojoToEntry implements Converter<Entry, com.trix.wowgarrisontracker.model.Entry> {

    @Autowired
    private AccountCharacterRepository accountCharacterRepository;

    public EntryPojoToEntry(AccountCharacterRepository accountCharacterRepository) {
        this.accountCharacterRepository = accountCharacterRepository;
    }

    @Override
    public com.trix.wowgarrisontracker.model.Entry convert(Entry source) {
        com.trix.wowgarrisontracker.model.Entry entry = new com.trix.wowgarrisontracker.model.Entry();
        if (source.getAccountCharacter() != null)
            entry.setAccountCharacter(source.getAccountCharacter());
        else
            entry.setAccountCharacter(accountCharacterRepository.findById(source.getAccountCharacterId()).get());
        entry.setEntryDate(source.getEntryDate());
        entry.setGarrisonResources(source.getGarrisonResources());
        entry.setId(source.getId());
        entry.setWarPaint(source.getWarPaint());
        return entry;
    }

}
