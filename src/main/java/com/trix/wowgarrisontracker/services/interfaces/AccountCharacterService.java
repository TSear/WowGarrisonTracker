package com.trix.wowgarrisontracker.services.interfaces;

import java.util.List;

import com.trix.wowgarrisontracker.model.AccountCharacter;
import com.trix.wowgarrisontracker.pojos.AccCharacterResourcesPojo;
import com.trix.wowgarrisontracker.pojos.AccountCharacterPojo;

public interface AccountCharacterService {
    
    List<AccountCharacter> listOfAccountCharacters(Long accountId);

    List<AccCharacterResourcesPojo> listOfResources(Long accountId);

    boolean isNameTaken(Long accountId, String name);

	void save(AccountCharacterPojo characterPojo);

	AccountCharacter findById(Long id);

}
