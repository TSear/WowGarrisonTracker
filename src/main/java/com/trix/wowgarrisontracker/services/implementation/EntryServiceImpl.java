package com.trix.wowgarrisontracker.services.implementation;

import java.util.List;

import org.springframework.stereotype.Service;

import com.trix.wowgarrisontracker.converters.EntryPojoToEntry;
import com.trix.wowgarrisontracker.model.Account;
import com.trix.wowgarrisontracker.model.AccountCharacter;
import com.trix.wowgarrisontracker.model.Entry;
import com.trix.wowgarrisontracker.pojos.EntryPojo;
import com.trix.wowgarrisontracker.repository.EntryRepository;
import com.trix.wowgarrisontracker.services.interfaces.AccountCharacterService;
import com.trix.wowgarrisontracker.services.interfaces.AccountService;
import com.trix.wowgarrisontracker.services.interfaces.EntryService;

@Service
public class EntryServiceImpl implements EntryService {

	private EntryRepository entryRepository;
	private EntryPojoToEntry entryPojoToEntry;
	private AccountCharacterService accountCharacterService;

	public EntryServiceImpl(EntryRepository entryRepository, EntryPojoToEntry entryPojoToEntry,
			AccountCharacterService accountCharacterService)  {
		this.entryRepository = entryRepository;
		this.entryPojoToEntry = entryPojoToEntry;
		this.accountCharacterService = accountCharacterService;
	}

	@Override
	public List<Entry> listOfEntries(Long characterId) {

		List<Entry> listOfEntries = entryRepository.findByAccountCharacterId(characterId);

		return listOfEntries;
	}

	@Override
    public boolean save(EntryPojo entryPojo) {
       entryRepository.save(entryPojoToEntry.convert(entryPojo));

        return true;
    }

}
