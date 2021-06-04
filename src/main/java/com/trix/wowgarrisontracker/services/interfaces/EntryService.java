package com.trix.wowgarrisontracker.services.interfaces;

import java.util.List;
import java.util.Optional;

import com.trix.wowgarrisontracker.model.Entry;
import com.trix.wowgarrisontracker.pojos.EntryPojo;
import org.springframework.data.domain.Pageable;

public interface EntryService {

    List<Entry> findAllByAccountCharacterId(Long accountCharacterId);

    void save(EntryPojo entryPojo);

    int getAmountOfDays(Long id);


    void delete(Long id);

    Entry findById(Long id);

    List<Entry> getAllAccountEntriesPaged(Long accountId, Pageable pageable);

    List<EntryPojo> getAllAccountEntriesPagedPojo(Long id, Long offset, Long limit);

    int getAmountOfEntries(Long accountId);
}