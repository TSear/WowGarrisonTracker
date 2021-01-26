package com.trix.wowgarrisontracker.services.implementation;

import java.util.ArrayList;
import java.util.List;

import com.trix.wowgarrisontracker.model.AccountCharacter;
import com.trix.wowgarrisontracker.pojos.AccCharacterResourcesPojo;
import com.trix.wowgarrisontracker.repository.AccountCharacterRepository;
import com.trix.wowgarrisontracker.repository.EntryRepository;
import com.trix.wowgarrisontracker.services.interfaces.AccountCharacterService;

import org.springframework.stereotype.Service;

@Service
public class AccountCharacterServiceImpl implements AccountCharacterService {

    private AccountCharacterRepository accountCharacterRepository;
    private EntryRepository entryRepository;

    public AccountCharacterServiceImpl(AccountCharacterRepository accountCharacterRepository, EntryRepository entryRepository) {
        this.accountCharacterRepository = accountCharacterRepository;
        this.entryRepository = entryRepository;
    }

    @Override
    public List<AccountCharacter> listOfAccountCharacters(Long accountId) {
        return accountCharacterRepository.findAll();
    }

    @Override
    public List<AccCharacterResourcesPojo> listOfResources(Long accountId) {
        
        List<AccCharacterResourcesPojo> accResourcerList = new ArrayList<>();

        for(AccountCharacter tmpAccCharacter : this.listOfAccountCharacters(accountId)){
            AccCharacterResourcesPojo accCharacterResourcesPojo = new AccCharacterResourcesPojo();
            accCharacterResourcesPojo.setCharacterName(tmpAccCharacter.getCharacterName());
            accCharacterResourcesPojo.setWarPaint(entryRepository.getWarPaintByCharacterId(tmpAccCharacter.getAccountId()));
            accCharacterResourcesPojo.setGarrisonResources(entryRepository.getGarrisonResourcesByCharacterId(tmpAccCharacter.getAccountId()));
            accCharacterResourcesPojo.setId(tmpAccCharacter.getId());
            accResourcerList.add(accCharacterResourcesPojo);
        }

        return accResourcerList;
    }
    
}
