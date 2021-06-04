package com.trix.wowgarrisontracker.services.implementation;

import com.trix.wowgarrisontracker.converters.EntryPojoToEntry;
import com.trix.wowgarrisontracker.converters.EntryToEntryPojo;
import com.trix.wowgarrisontracker.model.Entry;
import com.trix.wowgarrisontracker.pojos.EntryPojo;
import com.trix.wowgarrisontracker.repository.EntryRepository;
import com.trix.wowgarrisontracker.services.interfaces.AccountCharacterService;
import com.trix.wowgarrisontracker.services.interfaces.EntryService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EntryServiceImpl implements EntryService {

    private final EntryRepository entryRepository;
    private final EntryPojoToEntry entryPojoToEntry;
    private final AccountCharacterService accountCharacterService;
    private final EntryToEntryPojo entryToEntryPojo;

    public EntryServiceImpl(EntryRepository entryRepository, EntryPojoToEntry entryPojoToEntry,
                            AccountCharacterService accountCharacterService,
                            EntryToEntryPojo entryToEntryPojo) {
        this.entryRepository = entryRepository;
        this.entryPojoToEntry = entryPojoToEntry;
        this.accountCharacterService = accountCharacterService;
        this.entryToEntryPojo = entryToEntryPojo;
    }

    @Override
    public List<Entry> findAllByAccountCharacterId(Long accountCharacterId) {
        return entryRepository.findAllByAccountCharacterId(accountCharacterId);
    }


    @Override
    public void save(EntryPojo entryPojo) {
        Entry entryConverted = entryPojoToEntry.convert(entryPojo);
        accountCharacterService.addNewEntryToAccountCharacter(entryConverted);
        entryRepository.save(entryConverted);
    }

    @Override
    public int getAmountOfDays(Long id) {

        List<Long> listOfEntriesEachDay = entryRepository.getListOfEntriesEachDay(id);
        return listOfEntriesEachDay.size();
    }


    @Override
    public void delete(Long id) {
        Entry entry = this.findById(id);
        accountCharacterService.removeEntryFromAccountCharacter(entry);
        entryRepository.deleteById(id);
    }


    @Override
    public Entry findById(Long id) {
        Optional<Entry> optionalEntry = entryRepository.findById(id);

        if (optionalEntry.isEmpty())
            throw new RuntimeException("Entity with id: " + id + "was not found");

        return optionalEntry.get();

    }

    @Override
    public List<Entry> getAllAccountEntriesPaged(Long accountId, Pageable pageable) {
        return entryRepository.findAllEntriesByAccountId(accountId, pageable);
    }

    @Override
    public List<EntryPojo> getAllAccountEntriesPagedPojo(Long id, Long offset, Long limit) {
        List<Entry> entries = this.getAllAccountEntriesPaged(id, PageRequest.of(offset.intValue(), limit.intValue()));

        return entries.stream().map(entryToEntryPojo::convert).collect(Collectors.toList());

    }

    @Override
    public int getAmountOfEntries(Long accountId) {
        return entryRepository.getAmountOfEntries(accountId);
    }


}
