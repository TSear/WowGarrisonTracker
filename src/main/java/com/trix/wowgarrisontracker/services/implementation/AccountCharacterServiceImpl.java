package com.trix.wowgarrisontracker.services.implementation;

import com.trix.wowgarrisontracker.converters.AccountCharacterToAccountCharacterPojo;
import com.trix.wowgarrisontracker.model.AccountCharacter;
import com.trix.wowgarrisontracker.pojos.AccountCharacterForm;
import com.trix.wowgarrisontracker.pojos.AccountCharacterPojo;
import com.trix.wowgarrisontracker.repository.AccountCharacterRepository;
import com.trix.wowgarrisontracker.services.interfaces.AccountCharacterService;
import com.trix.wowgarrisontracker.services.interfaces.AccountService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AccountCharacterServiceImpl implements AccountCharacterService {

    private final AccountCharacterRepository accountCharacterRepository;
    private final AccountCharacterToAccountCharacterPojo accountCharacterToAccountCharacterPojo;
    private final AccountService accountService;

    public AccountCharacterServiceImpl(AccountCharacterRepository accountCharacterRepository,
                                       AccountCharacterToAccountCharacterPojo accountCharacterToAccountCharacterPojo,
                                       AccountService accountService) {
        this.accountCharacterRepository = accountCharacterRepository;
        this.accountCharacterToAccountCharacterPojo = accountCharacterToAccountCharacterPojo;
        this.accountService = accountService;
    }

    @Override
    public AccountCharacter findById(Long id) {
        Optional<AccountCharacter> optionalAccountCharacter = accountCharacterRepository.findById(id);
        return optionalAccountCharacter.orElse(null);
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
    public boolean save(AccountCharacterForm accountCharacterForm) {

        if (accountCharacterForm != null) {
            AccountCharacter accountCharacter = new AccountCharacter();
            accountCharacter.setAccount(accountService.findById(accountCharacterForm.getAccountId()));
            accountCharacter.setCharacterName(accountCharacterForm.getAccountCharacterName());
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
