package com.trix.wowgarrisontracker.services.implementation;

import com.trix.wowgarrisontracker.converters.AccountCharacterPojoToAccountCharacter;
import com.trix.wowgarrisontracker.converters.AccountCharacterToAccountCharacterPojo;
import com.trix.wowgarrisontracker.converters.EntryPojoToEntry;
import com.trix.wowgarrisontracker.model.AccountCharacter;
import com.trix.wowgarrisontracker.model.Entry;
import com.trix.wowgarrisontracker.pojos.AccountCharacterPojo;
import com.trix.wowgarrisontracker.pojos.EntryPojo;
import com.trix.wowgarrisontracker.repository.AccountCharacterRepository;
import com.trix.wowgarrisontracker.services.interfaces.AccountCharacterService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AccountCharacterServiceImpl implements AccountCharacterService {

    private AccountCharacterRepository accountCharacterRepository;
    private AccountCharacterPojoToAccountCharacter accountCharacterPojoToAccountCharacter;
    private AccountCharacterToAccountCharacterPojo accountCharacterToAccountCharacterPojo;

    public AccountCharacterServiceImpl(AccountCharacterRepository accountCharacterRepository,
                                       AccountCharacterPojoToAccountCharacter accountCharacterPojoToAccountCharacter,
                                       AccountCharacterToAccountCharacterPojo accountCharacterToAccountCharacterPojo) {
        this.accountCharacterRepository = accountCharacterRepository;
        this.accountCharacterPojoToAccountCharacter = accountCharacterPojoToAccountCharacter;
        this.accountCharacterToAccountCharacterPojo = accountCharacterToAccountCharacterPojo;
    }

    @Override
    public AccountCharacter findById(Long id) {
        Optional<AccountCharacter> optionalAccountCharacter = accountCharacterRepository.findById(id);
        return optionalAccountCharacter.isPresent() ? optionalAccountCharacter.get() : null;
    }

    @Override
    public void addNewEntryToAccountCharacter(Entry entry) {
        AccountCharacter accountCharacter = entry.getAccountCharacter();
        accountCharacter.addNewEntry(entry);
        accountCharacterRepository.save(accountCharacter);
    }

    @Override
    public void removeEntryFromAccountCharacter(Entry entry) {
        if (entry != null && entry.getAccountCharacter() != null) {
            AccountCharacter accountCharacter = entry.getAccountCharacter();
            entry.getAccountCharacter().removeEntry(entry);
            accountCharacterRepository.save(accountCharacter);
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
