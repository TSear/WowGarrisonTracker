package com.trix.wowgarrisontracker.services.implementation;

import com.trix.wowgarrisontracker.converters.EntryPojoToEntry;
import com.trix.wowgarrisontracker.model.Entry;
import com.trix.wowgarrisontracker.pojos.EntryPojo;
import com.trix.wowgarrisontracker.repository.EntryRepository;
import com.trix.wowgarrisontracker.services.interfaces.AccountCharacterService;
import com.trix.wowgarrisontracker.services.interfaces.EntryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EntryServiceImpl implements EntryService {

    private EntryRepository entryRepository;
    private EntryPojoToEntry entryPojoToEntry;
    private AccountCharacterService accountCharacterService;

    public EntryServiceImpl(EntryRepository entryRepository, EntryPojoToEntry entryPojoToEntry,
                            AccountCharacterService accountCharacterService) {
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

        Entry entry = entryPojoToEntry.convert(entryPojo);
        entryRepository.save(entry);
        accountCharacterService.updateAccountCharacterGarrisonResourcesAndWarPaint(entry);
        return true;
    }

    @Override
    public int getAmountOfDays(Long id) {

        List<Long> listOfEntriesEachDay = entryRepository.getListOfEntriesEachDay(id);

        return listOfEntriesEachDay.size();
    }

}
