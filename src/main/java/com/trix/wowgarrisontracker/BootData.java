package com.trix.wowgarrisontracker;

import com.mysql.cj.util.TestUtils;
import com.trix.wowgarrisontracker.model.*;
import com.trix.wowgarrisontracker.repository.*;
import com.trix.wowgarrisontracker.services.interfaces.AccountCharacterService;
import com.trix.wowgarrisontracker.services.interfaces.AuctionService;
import com.trix.wowgarrisontracker.services.interfaces.ServerService;
import com.trix.wowgarrisontracker.utils.BlizzardRequestUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.w3c.dom.ranges.Range;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class BootData implements CommandLineRunner {

    private final AccountRepository accountRepository;
    private final AccountCharacterRepository accountCharacterRepository;
    private final EntryRepository entryRepository;
    private final ItemEntityRepository itemEntityRepository;
    private final AuctionEntityRepository auctionEntityRepository;
    private final AuctionService auctionService;
    private final BlizzardRequestUtils blizzardRequestUtils;
    private final ServerService serverService;
    private final PasswordEncoder passwordEncoder;
    private final AccountCharacterService accountCharacterService;

    public BootData(AccountRepository accountRepository, AccountCharacterRepository accountCharacterRepository,
                    EntryRepository entryRepository, ItemEntityRepository itemEntityRepository,
                    AuctionEntityRepository auctionEntityRepository, AuctionService auctionService,
                    ServerService serverService, PasswordEncoder passwordEncoder,
                    BlizzardRequestUtils blizzardRequestUtils,
                    AccountCharacterService accountCharacterService) {
        this.accountRepository = accountRepository;
        this.accountCharacterRepository = accountCharacterRepository;
        this.entryRepository = entryRepository;
        this.itemEntityRepository = itemEntityRepository;
        this.auctionEntityRepository = auctionEntityRepository;
        this.auctionService = auctionService;
        this.blizzardRequestUtils = blizzardRequestUtils;
        this.serverService = serverService;
        this.passwordEncoder = passwordEncoder;
        this.accountCharacterService = accountCharacterService;
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

        List<AccountCharacter> characters = IntStream.range(0,30).mapToObj(i -> {
            AccountCharacter accountCharacter = new AccountCharacter();
            accountCharacter.setAccount(account1);
            accountCharacter.setId((long)i);
            accountCharacter.setCharacterName("Character: " + i);
            return accountCharacter;
        }).map(accountCharacterRepository::save).collect(Collectors.toList());

        characters.forEach(accCharacter -> IntStream.range(0,5)
                .mapToObj(i -> generateEntryWithCharacter((int)(Math.random()*100), (int)(Math.random()*100), accCharacter))
                .forEach(accountCharacterService::addNewEntryToAccountCharacter));

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

    public static Entry generateEntryWithCharacter(int garrisonResources, int warPaint, AccountCharacter accountCharacter) {
        Entry entry = generateEntry(garrisonResources,warPaint);
        entry.setAccountCharacter(accountCharacter);
        accountCharacter.addNewEntry(entry);
        return entry;
    }

    private static Entry generateEntry(int garrisonResources, int warPaint){
        Entry entry = new Entry();
        entry.setWarPaint(warPaint);
        entry.setGarrisonResources(garrisonResources);
        return entry;
    }
}
