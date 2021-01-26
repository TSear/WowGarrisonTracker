package com.trix.wowgarrisontracker.services.interfaces;

import java.util.List;

import com.trix.wowgarrisontracker.model.AccountCharacter;
import com.trix.wowgarrisontracker.pojos.AccCharacterResourcesPojo;

public interface AccountCharacterService {
    
    List<AccountCharacter> listOfAccountCharacters(Long accountId);

    List<AccCharacterResourcesPojo> listOfResources(Long accountId);

}
