package com.trix.wowgarrisontracker.services.implementation;

import java.util.List;

import com.trix.wowgarrisontracker.model.AccountCharacter;
import com.trix.wowgarrisontracker.repository.AccountCharacterRepository;
import com.trix.wowgarrisontracker.services.interfaces.AccountCharacterService;

import org.springframework.stereotype.Service;

@Service
public class AccountCharacterServiceImpl implements AccountCharacterService {

    private AccountCharacterRepository accountCharacterRepository;

    public AccountCharacterServiceImpl(AccountCharacterRepository accountCharacterRepository) {
        this.accountCharacterRepository = accountCharacterRepository;
    }

    @Override
    public List<AccountCharacter> listOfAccountCharacters(Long accountId) {
        return accountCharacterRepository.findAll();
    }
    
}
