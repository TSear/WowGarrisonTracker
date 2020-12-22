package com.trix.wowgarrisontracker.converters;

import java.util.Optional;

import com.trix.wowgarrisontracker.model.AccountCharacter;
import com.trix.wowgarrisontracker.model.Entry;
import com.trix.wowgarrisontracker.pojos.EntryPojo;
import com.trix.wowgarrisontracker.repository.AccountCharacterRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
public class EntryPojoToEntry implements Converter<EntryPojo, Entry>{

    @Autowired
    private AccountCharacterRepository accountCharacterRepository;

    private Logger logger = LoggerFactory.getLogger(Slf4j.class);



    @Override
    public Entry convert(EntryPojo source) {
        Entry entry = new Entry();
        Optional<AccountCharacter> optionalAccountCharacter = accountCharacterRepository.findById(source.getAccountCharacterId());
        if(optionalAccountCharacter.isPresent())
            entry.setAccountCharacterId(optionalAccountCharacter.get());
        else
            logger.error("Nie znaleziono postaci");

        entry.setEntryDate(source.getEntryDate());
        entry.setGarrisonResources(source.getGarrisonResources());
        entry.setId(source.getId());
        entry.setWarPaint(source.getWarPaint());
        return entry;
    }

    
    
}
