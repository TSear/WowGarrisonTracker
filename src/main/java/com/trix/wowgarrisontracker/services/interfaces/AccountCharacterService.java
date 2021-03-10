package com.trix.wowgarrisontracker.services.interfaces;

import com.trix.wowgarrisontracker.model.AccountCharacter;
import com.trix.wowgarrisontracker.model.Entry;
import com.trix.wowgarrisontracker.pojos.AccCharacterResourcesPojo;
import com.trix.wowgarrisontracker.pojos.AccountCharacterPojo;

import java.util.List;


public interface AccountCharacterService {

    void updateAccountCharacterGarrisonResourcesAndWarPaint(Entry entry);

    List<AccountCharacter> listOfAccountCharacters(Long accountId);


    boolean isNameTaken(Long accountId, String name);

    void save(AccountCharacterPojo characterPojo);

    AccountCharacter findById(Long id);

    List<AccountCharacterPojo> getListOfAccountCharactersConvertedToPojo(Long characterId);

    List<AccountCharacter> findAllByAccountId(Long accountId);

    void delete(Long id);
}
