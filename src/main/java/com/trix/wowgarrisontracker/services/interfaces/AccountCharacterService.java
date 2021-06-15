package com.trix.wowgarrisontracker.services.interfaces;

import com.trix.wowgarrisontracker.model.AccountCharacter;
import com.trix.wowgarrisontracker.pojos.AccountCharacterPojo;
import com.trix.wowgarrisontracker.pojos.Entry;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface AccountCharacterService {

    boolean addNewEntryToAccountCharacter(Entry entryPojo);

    boolean addNewEntryToAccountCharacter(com.trix.wowgarrisontracker.model.Entry entry);

    boolean removeEntryFromAccountCharacter(Entry entryPojo);

    boolean removeEntryFromAccountCharacter(com.trix.wowgarrisontracker.model.Entry entry);

    List<AccountCharacter> listOfAccountCharacters(Long accountId);


    boolean isNameTaken(Long accountId, String name);

    boolean save(AccountCharacterPojo characterPojo);

    boolean save(AccountCharacter accountCharacter);

    AccountCharacter findById(Long id);

    List<AccountCharacterPojo> getListOfAccountCharactersConvertedToPojo(Long characterId);

    List<AccountCharacter> findAllByAccountId(Long accountId);

    List<AccountCharacter> findAllByAccountId(Long accountId, Pageable pageable);


    void delete(Long id);

    List<AccountCharacterPojo> getAllAccountCharactersPagedPojo(Long id, int offset, int l);

    List<AccountCharacter> getAllAccountCharacterPaged(Long id, Pageable pageable);

    int countAllAccountCharactersByAccountId(Long id);

}
