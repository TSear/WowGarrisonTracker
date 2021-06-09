package com.trix.wowgarrisontracker.services.implementation;

import com.trix.wowgarrisontracker.converters.AccountCharacterPojoToAccountCharacter;
import com.trix.wowgarrisontracker.converters.AccountCharacterToAccountCharacterPojo;
import com.trix.wowgarrisontracker.converters.EntryPojoToEntry;
import com.trix.wowgarrisontracker.model.AccountCharacter;
import com.trix.wowgarrisontracker.pojos.AccountCharacterPojo;
import com.trix.wowgarrisontracker.pojos.Entry;
import com.trix.wowgarrisontracker.repository.AccountCharacterRepository;
import com.trix.wowgarrisontracker.services.interfaces.AccountCharacterService;
import com.trix.wowgarrisontracker.services.interfaces.EntryService;
import com.vaadin.flow.component.page.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AccountCharacterServiceImpl implements AccountCharacterService {

    private final AccountCharacterRepository accountCharacterRepository;
    private final AccountCharacterPojoToAccountCharacter accountCharacterPojoToAccountCharacter;
    private final AccountCharacterToAccountCharacterPojo accountCharacterToAccountCharacterPojo;
    private final EntryPojoToEntry entryPojoToEntry;
    private final EntryService entryService;

    public AccountCharacterServiceImpl(AccountCharacterRepository accountCharacterRepository,
                                       AccountCharacterPojoToAccountCharacter accountCharacterPojoToAccountCharacter,
                                       AccountCharacterToAccountCharacterPojo accountCharacterToAccountCharacterPojo,
                                       EntryService entryService,
                                       EntryPojoToEntry entryPojoToEntry) {
        this.accountCharacterRepository = accountCharacterRepository;
        this.accountCharacterPojoToAccountCharacter = accountCharacterPojoToAccountCharacter;
        this.accountCharacterToAccountCharacterPojo = accountCharacterToAccountCharacterPojo;
        this.entryService = entryService;
        this.entryPojoToEntry = entryPojoToEntry;
    }

    @Override
    public AccountCharacter findById(Long id) {
        Optional<AccountCharacter> optionalAccountCharacter = accountCharacterRepository.findById(id);
        return optionalAccountCharacter.orElse(null);
    }

    @Override
    public boolean addNewEntryToAccountCharacter(Entry entryPojo) {
        com.trix.wowgarrisontracker.model.Entry entry = entryPojoToEntry.convert(entryPojo);
        return this.addNewEntryToAccountCharacter(entry);
    }

    @Override
    public boolean addNewEntryToAccountCharacter(com.trix.wowgarrisontracker.model.Entry entry) {
        if (entry != null && entry.getAccountCharacter() != null) {
            AccountCharacter accountCharacter = entry.getAccountCharacter();
            accountCharacter.addResources(entry);
            accountCharacterRepository.save(accountCharacter);
            entryService.save(entry);
            return true;
        }
        return false;
    }

    @Override
    public boolean removeEntryFromAccountCharacter(Entry entryPojo) {
        com.trix.wowgarrisontracker.model.Entry entry = entryPojoToEntry.convert(entryPojo);
        return removeEntryFromAccountCharacter(entry);
    }

    @Override
    public boolean removeEntryFromAccountCharacter(com.trix.wowgarrisontracker.model.Entry entry) {
        if (entry != null && entry.getAccountCharacter() != null) {
            AccountCharacter accountCharacter = entry.getAccountCharacter();
            accountCharacter.removeResources(entry);
            accountCharacterRepository.save(accountCharacter);
            entryService.delete(entry.getId());
            return true;
        }
        return false;
    }


    @Override
    public List<AccountCharacter> listOfAccountCharacters(Long accountId) {
        return accountCharacterRepository.findAllByAccountId(accountId);
    }


    @Override
    public boolean isNameTaken(Long accountId, String name) {

        Optional<AccountCharacter> optionalAccountCharacter = accountCharacterRepository.findAllByAccountId(accountId)
                .stream()
                .filter(accountCharacter -> accountCharacter.getCharacterName().equals(name))
                .findFirst();

        return optionalAccountCharacter.isPresent();
    }

    @Override
    public boolean save(AccountCharacterPojo characterPojo) {

        if (characterPojo != null) {
            AccountCharacter accountCharacter = accountCharacterPojoToAccountCharacter.convert(characterPojo);
            return save(accountCharacter);
        }
        return false;

    }

    @Override
    public boolean save(AccountCharacter accountCharacter) {
       if(accountCharacter != null) {
           accountCharacterRepository.save(accountCharacter);
           return true;
       }
       return  false;
    }


    @Override
    public List<AccountCharacterPojo> getListOfAccountCharactersConvertedToPojo(Long accountId) {
        return this.listOfAccountCharacters(accountId)
                .stream()
                .map(accountCharacterToAccountCharacterPojo::convert)
                .collect(Collectors.toList());
    }

    @Override
    public List<AccountCharacter> findAllByAccountId(Long accountId) {
        return accountCharacterRepository.findAllByAccountId(accountId);
    }

    @Override
    public List<AccountCharacter> findAllByAccountId(Long accountId, Pageable pageable) {
        return null;
    }

    @Override
    public void delete(Long id) {
        accountCharacterRepository.deleteById(id);
    }



    @Override
    public List<AccountCharacterPojo> getAllAccountCharactersPagedPojo(Long id, int offset, int l) {
        List<AccountCharacter> accountCharacters = getAllAccountCharacterPaged(id, PageRequest.of(offset, l));
        return accountCharacters.stream().map(accountCharacterToAccountCharacterPojo::convert).collect(Collectors.toList());
    }

    @Override
    public List<AccountCharacter> getAllAccountCharacterPaged(Long id, Pageable pageable) {
        return accountCharacterRepository.findAllAccountCharactersByAccountPaged(id,pageable);
    }

    @Override
    public int countAllAccountCharactersByAccountId(Long id) {
        return accountCharacterRepository.countAccountCharacterByAccountId(id);
    }
}
