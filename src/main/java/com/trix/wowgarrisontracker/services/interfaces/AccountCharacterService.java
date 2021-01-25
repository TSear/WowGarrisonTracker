package com.trix.wowgarrisontracker.services.interfaces;

import java.util.List;

import com.trix.wowgarrisontracker.model.AccountCharacter;

public interface AccountCharacterService {
    
    List<AccountCharacter> listOfAccountCharacters(Long accountId);

}
