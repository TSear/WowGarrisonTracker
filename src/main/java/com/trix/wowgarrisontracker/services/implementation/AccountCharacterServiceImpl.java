package com.trix.wowgarrisontracker.services.implementation;

import com.trix.wowgarrisontracker.converters.AccountCharacterPojoToAccountCharacter;
import com.trix.wowgarrisontracker.converters.AccountCharacterToAccountCharacterPojo;
import com.trix.wowgarrisontracker.model.AccountCharacter;
import com.trix.wowgarrisontracker.model.Entry;
import com.trix.wowgarrisontracker.pojos.AccountCharacterPojo;
import com.trix.wowgarrisontracker.repository.AccountCharacterRepository;
import com.trix.wowgarrisontracker.services.interfaces.AccountCharacterService;
import com.trix.wowgarrisontracker.services.interfaces.EntryService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AccountCharacterServiceImpl implements AccountCharacterService {

    private final AccountCharacterRepository accountCharacterRepository;
    private final AccountCharacterPojoToAccountCharacter accountCharacterPojoToAccountCharacter;
    private final AccountCharacterToAccountCharacterPojo accountCharacterToAccountCharacterPojo;
    private final EntryService entryService;

    public AccountCharacterServiceImpl(AccountCharacterRepository accountCharacterRepository,
                                       AccountCharacterPojoToAccountCharacter accountCharacterPojoToAccountCharacter,
                                       AccountCharacterToAccountCharacterPojo accountCharacterToAccountCharacterPojo,
                                       EntryService entryService) {
        this.accountCharacterRepository = accountCharacterRepository;
        this.accountCharacterPojoToAccountCharacter = accountCharacterPojoToAccountCharacter;
        this.accountCharacterToAccountCharacterPojo = accountCharacterToAccountCharacterPojo;
        this.entryService = entryService;
    }

    @Override
    public AccountCharacter findById(Long id) {
        Optional<AccountCharacter> optionalAccountCharacter = accountCharacterRepository.findById(id);
        return optionalAccountCharacter.orElse(null);
    }

    @Override
    public void addNewEntryToAccountCharacter(Entry entry) {
        if (entry != null && entry.getAccountCharacter() != null) {
            AccountCharacter accountCharacter = entry.getAccountCharacter();
            accountCharacter.addResources(entry);
            accountCharacterRepository.save(accountCharacter);
            entryService.save(entry);
        }

    }

    @Override
    public void removeEntryFromAccountCharacter(Entry entry) {
        if (entry != null && entry.getAccountCharacter() != null) {
            AccountCharacter accountCharacter = entry.getAccountCharacter();
            accountCharacter.removeResources(entry);
            accountCharacterRepository.save(accountCharacter);
            entryService.delete(entry.getId());
        }
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
    public void save(AccountCharacterPojo characterPojo) {

        AccountCharacter accountCharacter = accountCharacterPojoToAccountCharacter.convert(characterPojo);
        accountCharacterRepository.save(accountCharacter);

    }

    @Override
    public List<AccountCharacterPojo> getListOfAccountCharactersConvertedToPojo(Long accountId) {
        List<AccountCharacterPojo> accountCharacterPojoList = this.listOfAccountCharacters(accountId)
                .stream()
                .map(accountCharacterToAccountCharacterPojo::convert)
                .collect(Collectors.toList());
        return accountCharacterPojoList;
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
}
