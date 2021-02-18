package com.trix.wowgarrisontracker.services.interfaces;

import java.util.List;

import com.trix.wowgarrisontracker.model.Entry;
import com.trix.wowgarrisontracker.pojos.EntryPojo;

public interface EntryService{

    List<Entry> listOfEntries(Long characterId);

    boolean save(EntryPojo entryPojo);

}