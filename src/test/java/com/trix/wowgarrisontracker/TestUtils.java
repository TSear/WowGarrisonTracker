package com.trix.wowgarrisontracker;

import com.trix.wowgarrisontracker.model.AccountCharacter;
import com.trix.wowgarrisontracker.model.Entry;

public class TestUtils {

    public static long id = 1;

    public static Entry entryGenerator(int garrisonResources, int warPaint, AccountCharacter accountCharacter){
        Entry entry = new Entry();
        entry.setWarPaint(warPaint);
        entry.setGarrisonResources(garrisonResources);
        entry.setAccountCharacter(accountCharacter);
//        entry.setId(id++);
        accountCharacter.addNewEntry(entry);
        return entry;
    }
}
