package com.trix.wowgarrisontracker.services.implementation;

import java.util.List;

import com.trix.wowgarrisontracker.converters.EntryPojoToEntry;
import com.trix.wowgarrisontracker.model.Entry;
import com.trix.wowgarrisontracker.pojos.EntryPojo;
import com.trix.wowgarrisontracker.repository.EntryRepository;
import com.trix.wowgarrisontracker.services.interfaces.EntryService;

import org.springframework.stereotype.Service;

@Service
public class EntryServiceImpl implements EntryService {

    private EntryRepository entryRepository;
    private EntryPojoToEntry entryPojoToEntry;

    public EntryServiceImpl(EntryRepository entryRepository, EntryPojoToEntry entryPojoToEntry) {
        this.entryRepository = entryRepository;
        this.entryPojoToEntry = entryPojoToEntry;
    }

    @Override
    public List<Entry> listOfEntries(Long characterId) {
        return entryRepository.findAll();
    }

    @Override
    public boolean saveEntry(EntryPojo entryPojo) {
        
        entryRepository.save(entryPojoToEntry.convert(entryPojo));

        return true;
    }

    

    

}
