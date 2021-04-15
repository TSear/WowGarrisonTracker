package com.trix.wowgarrisontracker;

import com.trix.wowgarrisontracker.model.*;
import com.trix.wowgarrisontracker.repository.*;
import com.trix.wowgarrisontracker.services.interfaces.AuctionService;
import com.trix.wowgarrisontracker.services.interfaces.ServerService;
import com.trix.wowgarrisontracker.utils.BlizzardRequestUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BootData implements CommandLineRunner {

    private AccountRepository accountRepository;
    private AccountCharacterRepository accountCharacterRepository;
    private EntryRepository entryRepository;
    private ItemEntityRepository itemEntityRepository;
    private AuctionEntityRepository auctionEntityRepository;
    private AuctionService auctionService;
    private BlizzardRequestUtils blizzardRequestUtils;
    private ServerService serverService;
    private PasswordEncoder passwordEncoder;

    public BootData(AccountRepository accountRepository, AccountCharacterRepository accountCharacterRepository,
                    EntryRepository entryRepository, ItemEntityRepository itemEntityRepository,
                    AuctionEntityRepository auctionEntityRepository, AuctionService auctionService,
                    ServerService serverService, PasswordEncoder passwordEncoder,
                    BlizzardRequestUtils blizzardRequestUtils) {
        this.accountRepository = accountRepository;
        this.accountCharacterRepository = accountCharacterRepository;
        this.entryRepository = entryRepository;
        this.itemEntityRepository = itemEntityRepository;
        this.auctionEntityRepository = auctionEntityRepository;
        this.auctionService = auctionService;
        this.blizzardRequestUtils = blizzardRequestUtils;
        this.serverService = serverService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        this.innit();
        auctionService.updateAuctionHouse();

    }

    private void innit() {

        List<Server> servers = blizzardRequestUtils.getListOfServers();
        servers.forEach(serverService::save);
        ItemEntity itemEntity = new ItemEntity();
        itemEntity.setId(109119L);
        itemEntity.setName("True Iron Ore");

        itemEntityRepository.save(itemEntity);

        auctionService.updateAuctionHouse();

        Account account1 = new Account();
        account1.setLogin("login1");
        account1.setPassword(passwordEncoder.encode("password1"));
        accountRepository.save(account1);
//
//        AccountCharacter accountCharacter = new AccountCharacter();
//        accountCharacter.setCharacterName("Calienda");
//        accountCharacter.setAccount(account1);
//        accountCharacterRepository.save(accountCharacter);
//        Entry entry1 = new Entry();
//        entry1.setWarPaint(150);
//        entry1.setGarrisonResources(250);
//        entry1.setAccountCharacter(accountCharacter);
//
//
//        //accountCharacter.setEntries(new HashSet<>(Arrays.asList(entry1)));
//        //setAccountCharacters(new HashSet<>(Arrays.asList(accountCharacter)));
//

//
//        AuctionEntity entity = new AuctionEntity();
//        entity.setId(1l);
//        entity.setItemId(109119l);
//        entity.setQuantity(500l);
//        entity.setUnitPrice(100000l);
//
//
//        itemEntityRepository.save(itemEntity);
//
//        auctionEntityRepository.save(entity);
//        entryRepository.save(entry1);
//
    }

}
