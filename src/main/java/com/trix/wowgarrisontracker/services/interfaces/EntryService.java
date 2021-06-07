package com.trix.wowgarrisontracker.services.interfaces;

import com.trix.wowgarrisontracker.model.Entry;
import com.trix.wowgarrisontracker.pojos.EntryPojo;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;

public interface EntryService {

    List<Entry> findAllByAccountCharacterId(Long accountCharacterId);

    boolean saveAll(Collection<Entry> entries);

    EntryPojo save(EntryPojo entryPojo);

    Entry save(Entry entry);

    int getAmountOfDays(Long id);


    void delete(Long id);

    Entry findById(Long id);

    List<Entry> getAllAccountEntriesPaged(Long accountId, Pageable pageable);

    List<EntryPojo> getAllAccountEntriesPagedPojo(Long id, Long offset, Long limit);

    int getAmountOfEntries(Long accountId);

    Long count();
}