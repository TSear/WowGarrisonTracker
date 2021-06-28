package com.trix.wowgarrisontracker.services.interfaces;

import com.trix.wowgarrisontracker.model.Entry;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;

public interface EntryService {

    List<Entry> findAllByAccountCharacterId(Long accountCharacterId);

    boolean saveAll(Collection<Entry> entries);


    Entry save(Entry entry);

    int getAmountOfDays(Long id);


    void delete(Long id);

    Entry findById(Long id);

    List<Entry> getAllAccountEntriesPaged(Long accountId, Pageable pageable);

    int getAmountOfEntries(Long accountId);

    Long count();
}