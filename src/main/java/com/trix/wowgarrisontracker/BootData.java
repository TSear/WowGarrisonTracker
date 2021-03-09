package com.trix.wowgarrisontracker;

import com.trix.wowgarrisontracker.model.*;
import com.trix.wowgarrisontracker.repository.AccountCharacterRepository;
import com.trix.wowgarrisontracker.repository.AccountRepository;
import com.trix.wowgarrisontracker.repository.AuctionEntityRepository;
import com.trix.wowgarrisontracker.repository.EntryRepository;
import com.trix.wowgarrisontracker.repository.ItemEntityRepository;
import com.trix.wowgarrisontracker.services.interfaces.AuctionService;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
public class BootData implements CommandLineRunner {

    private AccountRepository accountRepository;
    private AccountCharacterRepository accountCharacterRepository;
    private EntryRepository entryRepository;
    private ItemEntityRepository itemEntityRepository;
    private AuctionEntityRepository auctionEntityRepository;
    private AuctionService auctionService;

    public BootData(AccountRepository accountRepository, AccountCharacterRepository accountCharacterRepository,
            EntryRepository entryRepository,ItemEntityRepository itemEntityRepository,
            AuctionEntityRepository auctionEntityRepository, AuctionService auctionService) {
        this.accountRepository = accountRepository;
        this.accountCharacterRepository = accountCharacterRepository;
        this.entryRepository = entryRepository;
        this.itemEntityRepository = itemEntityRepository;
        this.auctionEntityRepository = auctionEntityRepository;
        this.auctionService = auctionService;
    }

    @Override
    public void run(String... args) throws Exception {
    	auctionService.updateAuctionHouse();
//        logger.info("Creating data");
//        this.innit();
        
    }

    private void innit() {

        Account account1 = new Account();
        account1.setLogin("login1");
        account1.setPassword("password1");
        accountRepository.save(account1);

        AccountCharacter accountCharacter = new AccountCharacter();
        accountCharacter.setCharacterName("Calienda");
        accountCharacter.setAccount(account1);
        accountCharacterRepository.save(accountCharacter);
        Entry entry1 = new Entry();
        entry1.setWarPaint(150);
        entry1.setGarrisonResources(250);
        entry1.setAccountCharacter(accountCharacter);


        //accountCharacter.setEntries(new HashSet<>(Arrays.asList(entry1)));
        //setAccountCharacters(new HashSet<>(Arrays.asList(accountCharacter)));

        ItemEntity itemEntity = new ItemEntity();
        itemEntity.setId(109119l);
        itemEntity.setName("True Iron Ore");
        
        AuctionEntity entity = new AuctionEntity();
        entity.setId(1l);
        entity.setItemId(109119l);
        entity.setQuantity(500l);
        entity.setUnitPrice(100000l);
        
        
        itemEntityRepository.save(itemEntity);
        
        auctionEntityRepository.save(entity);
        entryRepository.save(entry1);
        
    }

}
