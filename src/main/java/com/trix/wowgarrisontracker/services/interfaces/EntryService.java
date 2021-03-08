package com.trix.wowgarrisontracker.services.interfaces;

import java.util.List;

import com.trix.wowgarrisontracker.model.Entry;
import com.trix.wowgarrisontracker.pojos.EntryPojo;

public interface EntryService{

    List<Entry> accountEntriesList(Long accountId);

    void save(EntryPojo entryPojo);

    int getAmountOfDays(Long id);

    List<EntryPojo> accountEntriesConvertedToPojoList(Long accountId);
}