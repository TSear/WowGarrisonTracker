package com.trix.wowgarrisontracker.services.interfaces;

import com.trix.wowgarrisontracker.pojos.Entry;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;

public interface EntryService {

    List<com.trix.wowgarrisontracker.model.Entry> findAllByAccountCharacterId(Long accountCharacterId);

    boolean saveAll(Collection<com.trix.wowgarrisontracker.model.Entry> entries);

    Entry save(Entry entryPojo);

    com.trix.wowgarrisontracker.model.Entry save(com.trix.wowgarrisontracker.model.Entry entry);

    int getAmountOfDays(Long id);


    void delete(Long id);

    com.trix.wowgarrisontracker.model.Entry findById(Long id);

    List<com.trix.wowgarrisontracker.model.Entry> getAllAccountEntriesPaged(Long accountId, Pageable pageable);

    List<Entry> getAllAccountEntriesPagedPojo(Long id, Long offset, Long limit);

    int getAmountOfEntries(Long accountId);

    Long count();
}