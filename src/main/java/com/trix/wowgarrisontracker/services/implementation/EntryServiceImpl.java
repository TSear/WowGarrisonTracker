package com.trix.wowgarrisontracker.services.implementation;

import com.trix.wowgarrisontracker.repository.EntryRepository;
import com.trix.wowgarrisontracker.services.interfaces.EntryService;

public class EntryServiceImpl implements EntryService{
    
    private EntryRepository entryRepository;

    public EntryServiceImpl(EntryRepository entryRepository){
        this.entryRepository = entryRepository;
    }

}
