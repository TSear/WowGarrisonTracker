package com.trix.wowgarrisontracker.services.implementation;

import com.trix.wowgarrisontracker.model.Entry;
import com.trix.wowgarrisontracker.repository.EntryRepository;
import com.trix.wowgarrisontracker.services.interfaces.EntryService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class EntryServiceImpl implements EntryService {

    private final EntryRepository entryRepository;

    public EntryServiceImpl(EntryRepository entryRepository) {
        this.entryRepository = entryRepository;
    }

    @Override
    public List<Entry> findAllByAccountCharacterId(Long accountCharacterId) {
        return entryRepository.findAllByAccountCharacterId(accountCharacterId);
    }

    @Override
    public boolean saveAll(Collection<Entry> entries) {
        List<Entry> saved = entryRepository.saveAll(entries);
        return saved.size() == entries.size();
    }


    @Override
    public Entry save(Entry entry) {
        if (entry != null)
            return entryRepository.save(entry);
        return null;
    }

    @Override
    public int getAmountOfDays(Long id) {

        List<Long> listOfEntriesEachDay = entryRepository.getListOfEntriesEachDay(id);
        return listOfEntriesEachDay.size();
    }


    @Override
    public void delete(Long id) {
        entryRepository.deleteById(id);
    }


    @Override
    public Entry findById(Long id) throws RuntimeException {
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
    public int getAmountOfEntries(Long accountId) {
        return entryRepository.countEntriesByAccountCharacterId(accountId);
    }

    @Override
    public Long count() {
        return entryRepository.count();
    }


}
