package com.trix.wowgarrisontracker.services.interfaces;

import com.trix.wowgarrisontracker.model.AccountCharacter;
import com.trix.wowgarrisontracker.pojos.AccountCharacterForm;
import com.trix.wowgarrisontracker.pojos.AccountCharacterPojo;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface AccountCharacterService {


    List<AccountCharacter> listOfAccountCharacters(Long accountId);

    boolean isNameTaken(Long accountId, String name);

    boolean save(AccountCharacterForm accountCharacterForm);

    boolean save(AccountCharacter accountCharacter);

    AccountCharacter findById(Long id);

    List<AccountCharacterPojo> getListOfAccountCharactersConvertedToPojo(Long characterId);

    List<AccountCharacter> findAllByAccountId(Long accountId);

    void delete(Long id);

    List<AccountCharacterPojo> getAllAccountCharactersPagedPojo(Long id, int offset, int l);

    List<AccountCharacter> getAllAccountCharacterPaged(Long id, Pageable pageable);

    int countAllAccountCharactersByAccountId(Long id);

}
