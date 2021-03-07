package com.trix.wowgarrisontracker.converters;

import com.trix.wowgarrisontracker.model.Entry;
import com.trix.wowgarrisontracker.pojos.EntryPojo;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class EntryToEntryPojo implements Converter<Entry, EntryPojo> {

    @Override
    public EntryPojo convert(Entry source) {
        EntryPojo entryPojo = new EntryPojo();
        entryPojo.setEntryDate(source.getEntryDate());
        entryPojo.setGarrisonResources(source.getGarrisonResources());
        entryPojo.setId(source.getId());
        entryPojo.setWarPaint(source.getWarPaint());
        entryPojo.setCharacterName(source.getAccountCharacter().getCharacterName());

        return entryPojo;
    }

}