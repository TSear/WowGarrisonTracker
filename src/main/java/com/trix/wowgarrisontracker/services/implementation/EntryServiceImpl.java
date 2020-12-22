package com.trix.wowgarrisontracker.services.implementation;

import com.trix.wowgarrisontracker.repository.EntryRepository;
import com.trix.wowgarrisontracker.services.interfaces.EntryService;

import org.springframework.stereotype.Service;

@Service
public class EntryServiceImpl implements EntryService{
    
    private EntryRepository entryRepository;

    public EntryServiceImpl(EntryRepository entryRepository){
        this.entryRepository = entryRepository;
    }

}
