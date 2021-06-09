package com.trix.wowgarrisontracker.converters;

import com.trix.wowgarrisontracker.pojos.Entry;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class EntryToEntryPojo implements Converter<com.trix.wowgarrisontracker.model.Entry, Entry> {

    @Override
    public Entry convert(com.trix.wowgarrisontracker.model.Entry source) {
        Entry entryPojo = new Entry();
        entryPojo.setEntryDate(source.getEntryDate());
        entryPojo.setGarrisonResources(source.getGarrisonResources());
        entryPojo.setId(source.getId());
        entryPojo.setWarPaint(source.getWarPaint());
        entryPojo.setCharacterName(source.getAccountCharacter().getCharacterName());
        entryPojo.setAccountCharacterId(source.getAccountCharacter().getId());
        return entryPojo;
    }

}