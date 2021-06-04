package com.trix.wowgarrisontracker.converters;

import com.trix.wowgarrisontracker.model.Entry;
import com.trix.wowgarrisontracker.pojos.EntryPojo;
import com.trix.wowgarrisontracker.repository.AccountCharacterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class EntryPojoToEntry implements Converter<EntryPojo, Entry> {

    @Autowired
    private AccountCharacterRepository accountCharacterRepository;


    @Override
    public Entry convert(EntryPojo source) {
        Entry entry = new Entry();
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
