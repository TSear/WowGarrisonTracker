package com.trix.wowgarrisontracker;

import com.trix.wowgarrisontracker.blizzarapi.BlizzardApiRequests;
import com.trix.wowgarrisontracker.model.*;
import com.trix.wowgarrisontracker.repository.*;
import com.trix.wowgarrisontracker.services.interfaces.AccountCharacterService;
import com.trix.wowgarrisontracker.services.interfaces.AuctionService;
import com.trix.wowgarrisontracker.services.interfaces.ConnectedServersService;
import com.trix.wowgarrisontracker.services.interfaces.ServerService;
import com.trix.wowgarrisontracker.utils.BlizzardRequestUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
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
    private final BlizzardApiRequests blizzardApiRequests;
    private final ServerService serverService;
    private final PasswordEncoder passwordEncoder;
    private final AccountCharacterService accountCharacterService;
    private final ConnectedServersService connectedServersService;

    public BootData(AccountRepository accountRepository, AccountCharacterRepository accountCharacterRepository,
                    EntryRepository entryRepository, ItemEntityRepository itemEntityRepository,
                    AuctionEntityRepository auctionEntityRepository, AuctionService auctionService,
                    ServerService serverService, PasswordEncoder passwordEncoder,
                    BlizzardRequestUtils blizzardRequestUtils,
                    AccountCharacterService accountCharacterService,
                    BlizzardApiRequests blizzardApiRequests,
                    ConnectedServersService connectedServersService) {
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
        this.blizzardApiRequests = blizzardApiRequests;
        this.connectedServersService = connectedServersService;
    }

    @Override
    public void run(String... args) throws Exception {
        this.innit();
        auctionService.updateAuctionHouse();
    }

    private void innit() {

        if (serverService.getServers().size() == 0) {
            List<ConnectedServersModel> connectedServersModels = blizzardApiRequests.getListOfConnectedServers();
            connectedServersService.saveAll(connectedServersModels);
        }
//        Server server = new Server();
//        server.setId(1);
//        server.setRegion("EU");
//        server.setSlug("test");
//        server.setName("Test");

//        serverService.save(server);

        if (itemEntityRepository.count() == 0) {
            ItemEntity itemEntity = new ItemEntity();
            itemEntity.setId(109119L);
            itemEntity.setName("True Iron Ore");
            itemEntityRepository.save(itemEntity);
        }
//        auctionService.updateAuctionHouse();

        Account account1 = new Account();
        account1.setId(1L);
        account1.setLogin("login1");
        account1.setPassword(passwordEncoder.encode("password1"));

        if (accountRepository.count() == 0) {
            accountRepository.save(account1);
        }
        List<AccountCharacter> characters = IntStream.range(0, 30).mapToObj(i -> {
            AccountCharacter accountCharacter = new AccountCharacter();
            accountCharacter.setAccount(account1);
            accountCharacter.setId((long) i);
            accountCharacter.setCharacterName("Character: " + i);
            return accountCharacter;
        }).collect(Collectors.toList());
        if (accountCharacterRepository.count() == 0) {
            accountCharacterRepository.saveAll(characters);
        }

        if (entryRepository.count() == 0) {
            List<Entry> entries = new ArrayList<>();
            accountCharacterService.findAllByAccountId(1L).forEach(accCharacter -> IntStream.range(1, 5)
                    .mapToObj(i -> generateEntryWithCharacter((int) (Math.random() * 100), (int) (Math.random() * 100), accCharacter))
                    .forEach(entries::add));

            entryRepository.saveAll(entries);
        }


    }

    public static Entry generateEntryWithCharacter(int garrisonResources, int warPaint, AccountCharacter
            accountCharacter) {
        Entry entry = generateEntry(garrisonResources, warPaint);
        entry.setAccountCharacter(accountCharacter);
        accountCharacter.addNewEntry(entry);
        return entry;
    }

    private static Entry generateEntry(int garrisonResources, int warPaint) {
        Entry entry = new Entry();
        entry.setWarPaint(warPaint);
        entry.setGarrisonResources(garrisonResources);
        return entry;
    }
}
