package com.trix.wowgarrisontracker.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.trix.wowgarrisontracker.model.Entry;
import com.trix.wowgarrisontracker.pojos.EntryPojo;
import com.trix.wowgarrisontracker.services.interfaces.AccountCharacterService;

@Component
public class EntryToEntryPojo implements Converter<Entry, EntryPojo> {

	private AccountCharacterService accountCharacterService;
	
    /**
	 * @param accountCharacterService
	 */
	public EntryToEntryPojo(AccountCharacterService accountCharacterService) {
		this.accountCharacterService = accountCharacterService;
	}



	@Override
    public EntryPojo convert(Entry source) {
        EntryPojo entryPojo = new EntryPojo();
        entryPojo.setEntryDate(source.getEntryDate());
        entryPojo.setGarrisonResources(source.getGarrisonResources());
        entryPojo.setId(source.getId());
        entryPojo.setWarPaint(source.getWarPaint());
        entryPojo.setCharacterName(accountCharacterService.findById(source.getAccountCharacterId()).getCharacterName());
        //entryPojo.setAccountCharacterId(source.getAccountCharacterId().getId());

        return entryPojo;
    }

}