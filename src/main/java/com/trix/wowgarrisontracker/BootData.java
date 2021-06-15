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

        if (itemEntityRepository.count() == 0) {
            ItemEntity itemEntity = new ItemEntity();
            itemEntity.setId(109119L);
            itemEntity.setName("True Iron Ore");
            itemEntityRepository.save(itemEntity);
        }


        auctionService.updateAuctionHouse();
        if (accountRepository.count() == 0) {
            Account account1 = new Account();
            account1.setId(1L);
            account1.setLogin("login1");
            account1.setPassword(passwordEncoder.encode("password1"));
            accountRepository.save(account1);
            if (accountCharacterService.findAllByAccountId(account1.getId()).size() == 0) {
                List<AccountCharacter> characters = IntStream.range(0, 30).mapToObj(i -> {
                    AccountCharacter accountCharacter = new AccountCharacter();
                    accountCharacter.setAccount(account1);
                    accountCharacter.setId((long) i);
                    accountCharacter.setCharacterName("Character: " + i);
                    return accountCharacter;
                }).map(accountCharacterRepository::save).collect(Collectors.toList());
                characters.forEach(accCharacter -> IntStream.range(0, 5)
                        .mapToObj(i -> generateEntryWithCharacter((int) (Math.random() * 100), (int) (Math.random() * 100), accCharacter, (long) i))
                        .forEach(accountCharacterService::addNewEntryToAccountCharacter));
            }
        }


    }

    public static Entry generateEntryWithCharacter(int garrisonResources, int warPaint, AccountCharacter accountCharacter, Long id) {
        Entry entry = generateEntry(garrisonResources, warPaint);
        entry.setAccountCharacter(accountCharacter);
        entry.setId(id);
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
