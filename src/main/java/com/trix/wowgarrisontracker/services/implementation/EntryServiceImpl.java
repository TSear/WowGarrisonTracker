package com.trix.wowgarrisontracker.services.implementation;

import com.trix.wowgarrisontracker.converters.EntryPojoToEntry;
import com.trix.wowgarrisontracker.converters.EntryToEntryPojo;
import com.trix.wowgarrisontracker.model.AccountCharacter;
import com.trix.wowgarrisontracker.model.Entry;
import com.trix.wowgarrisontracker.pojos.EntryPojo;
import com.trix.wowgarrisontracker.repository.EntryRepository;
import com.trix.wowgarrisontracker.services.interfaces.AccountCharacterService;
import com.trix.wowgarrisontracker.services.interfaces.EntryService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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

        Entry entry = entryPojoToEntry.convert(entryPojo);
        entryRepository.save(entry);
        accountCharacterService.updateAccountCharacterGarrisonResourcesAndWarPaint(entry);
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

}
