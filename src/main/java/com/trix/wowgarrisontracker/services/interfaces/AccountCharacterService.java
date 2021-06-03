package com.trix.wowgarrisontracker.services.interfaces;

import com.trix.wowgarrisontracker.model.AccountCharacter;
import com.trix.wowgarrisontracker.model.Entry;
import com.trix.wowgarrisontracker.pojos.AccCharacterResourcesPojo;
import com.trix.wowgarrisontracker.pojos.AccountCharacterPojo;
import com.trix.wowgarrisontracker.pojos.EntryPojo;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface AccountCharacterService {

    void addNewEntryToAccountCharacter(Entry entry);

    void removeEntryFromAccountCharacter(Entry entry);

    List<AccountCharacter> listOfAccountCharacters(Long accountId);


    boolean isNameTaken(Long accountId, String name);

    void save(AccountCharacterPojo characterPojo);

    AccountCharacter findById(Long id);

    List<AccountCharacterPojo> getListOfAccountCharactersConvertedToPojo(Long characterId);

    List<AccountCharacter> findAllByAccountId(Long accountId);

    List<AccountCharacter> findAllByAccountId(Long accountId, Pageable pageable);



    void delete(Long id);
}
