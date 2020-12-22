package com.trix.wowgarrisontracker;

import java.util.Arrays;
import java.util.HashSet;

import com.trix.wowgarrisontracker.model.Account;
import com.trix.wowgarrisontracker.model.AccountCharacter;
import com.trix.wowgarrisontracker.model.Entry;
import com.trix.wowgarrisontracker.repository.AccountCharacterRepository;
import com.trix.wowgarrisontracker.repository.AccountRepository;
import com.trix.wowgarrisontracker.repository.EntryRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
public class BootData implements CommandLineRunner {

    private AccountRepository accountRepository;
    private AccountCharacterRepository accountCharacterRepository;
    private EntryRepository entryRepository;
    private Logger logger = LoggerFactory.getLogger(Slf4j.class);

    public BootData(AccountRepository accountRepository, AccountCharacterRepository accountCharacterRepository,
            EntryRepository entryRepository) {
        this.accountRepository = accountRepository;
        this.accountCharacterRepository = accountCharacterRepository;
        this.entryRepository = entryRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("Creating data");
        this.innit();
        
    }

    private void innit() {

        Account account1 = new Account();
        account1.setLogin("login1");
        account1.setPassword("password1");
        
        AccountCharacter accountCharacter = new AccountCharacter();
        accountCharacter.setCharacterName("characterName");
        accountCharacter.setAccountId(account1.getId());

        Entry entry1 = new Entry();
        entry1.setWarPaint(150);
        entry1.setGarrisonResources(250);
        entry1.setAccountCharacterId(accountCharacter);


        accountCharacter.setEntries(new HashSet<>(Arrays.asList(entry1)));

        account1.setAccountCharacters(new HashSet<>(Arrays.asList(accountCharacter)));

        accountRepository.save(account1);
        accountCharacterRepository.save(accountCharacter);
        entryRepository.save(entry1);
    }

}
