package com.trix.wowgarrisontracker.services.implementation;

import com.trix.wowgarrisontracker.converters.EntryPojoToEntry;
import com.trix.wowgarrisontracker.converters.EntryToEntryPojo;
import com.trix.wowgarrisontracker.model.AccountCharacter;
import com.trix.wowgarrisontracker.model.Entry;
import com.trix.wowgarrisontracker.pojos.EntryPojo;
import com.trix.wowgarrisontracker.repository.EntryRepository;
import com.trix.wowgarrisontracker.services.interfaces.AccountCharacterService;
import com.trix.wowgarrisontracker.services.interfaces.EntryService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EntryServiceImpl implements EntryService {

    private EntryRepository entryRepository;
    private EntryPojoToEntry entryPojoToEntry;
    private AccountCharacterService accountCharacterService;
    private EntryToEntryPojo entryToEntryPojo;

    public EntryServiceImpl(EntryRepository entryRepository, EntryPojoToEntry entryPojoToEntry,
                            AccountCharacterService accountCharacterService,
                            EntryToEntryPojo entryToEntryPojo) {
        this.entryRepository = entryRepository;
        this.entryPojoToEntry = entryPojoToEntry;
        this.accountCharacterService = accountCharacterService;
        this.entryToEntryPojo = entryToEntryPojo;
    }

    @Override
    public List<Entry> accountEntriesList(Long accountId) {
        List<AccountCharacter> accountCharacterList = accountCharacterService.findAllByAccountId(accountId);
        List<Entry> entryList = new ArrayList<>();
        accountCharacterList.stream()
                .forEach(accountCharacter -> entryList.addAll(accountCharacter.getEntries()));
        return entryList;
    }


    @Override
    public void save(EntryPojo entryPojo) {

        accountCharacterService.addNewEntryToAccountCharacter(entryPojoToEntry.convert(entryPojo));
        Entry entry = entryPojoToEntry.convert(entryPojo);
        entryRepository.save(entry);
    }

    @Override
    public int getAmountOfDays(Long id) {

        List<Long> listOfEntriesEachDay = entryRepository.getListOfEntriesEachDay(id);
        return listOfEntriesEachDay.size();
    }

    @Override
    public List<EntryPojo> accountEntriesConvertedToPojoList(Long accountId) {
        List<Entry> listOfAccountEntries = this.accountEntriesList(accountId);
        List<EntryPojo> listOfAccountEntriesConvertedToPojo = listOfAccountEntries.stream()
                .map(entryToEntryPojo::convert)
                .collect(Collectors.toList());
        return listOfAccountEntriesConvertedToPojo;
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

        if(optionalEntry.isEmpty())
            throw new RuntimeException("Entity with id: "+id +"was not found");

        return optionalEntry.get();

    }

    @Override
    public List<Entry> getAllAccountEntriesPaged(Long accountId, Pageable pageable) {
       return entryRepository.findAllEntriesByAccountId(accountId, pageable);
    }

    @Override
    public List<EntryPojo> getAllAccountEntriesPagedPojo(Long id, Long offset, Long limit) {
       List<Entry> entries = this.getAllAccountEntriesPaged(id, PageRequest.of(offset.intValue(),limit.intValue()));

       return entries.stream().map(entryToEntryPojo::convert).collect(Collectors.toList());

    }

    @Override
    public int getAmountOfEntries(Long accountId) {
        return entryRepository.getAmountOfEntries(accountId);
        }


}
