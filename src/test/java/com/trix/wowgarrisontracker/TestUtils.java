package com.trix.wowgarrisontracker;

import com.trix.wowgarrisontracker.model.Account;
import com.trix.wowgarrisontracker.model.AccountCharacter;
import com.trix.wowgarrisontracker.model.Entry;

public class TestUtils {

    public static long id = 1;

    public static Entry generateEntryWithCharacter(int garrisonResources, int warPaint, AccountCharacter accountCharacter) {
        Entry entry = generateEntry(garrisonResources,warPaint);
        entry.setAccountCharacter(accountCharacter);
        accountCharacter.addNewEntry(entry);
        return entry;
    }

    public static Entry generateEntryNoCharacter(int garrisonResources, int warPaint){
       Entry entry = generateEntry(garrisonResources,warPaint);
       entry.setAccountCharacter(null);
       return entry;
    }

    private static Entry generateEntry(int garrisonResources, int warPaint){
        Entry entry = new Entry();
        entry.setWarPaint(warPaint);
        entry.setGarrisonResources(garrisonResources);
        return entry;
    }

    public static AccountCharacter createAccountCharacter(String characterName, Long id, Account account){
        AccountCharacter accountCharacter = new AccountCharacter();
        accountCharacter.setId(id);
        accountCharacter.setCharacterName(characterName);
        accountCharacter.setAccount(account);
        return accountCharacter;
    }
}
