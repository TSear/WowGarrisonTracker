package com.trix.wowgarrisontracker;

import com.trix.wowgarrisontracker.blizzarapi.AuctionHouseUpdaterThread;
import com.trix.wowgarrisontracker.blizzarapi.BlizzardApiRequests;
import com.trix.wowgarrisontracker.model.ConnectedServersModel;
import com.trix.wowgarrisontracker.model.ItemEntity;
import com.trix.wowgarrisontracker.services.interfaces.ConnectedServersService;
import com.trix.wowgarrisontracker.services.interfaces.ItemsService;
import com.trix.wowgarrisontracker.services.interfaces.ServerService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class BootData implements CommandLineRunner {

    private final ItemsService itemsService;
    private final BlizzardApiRequests blizzardApiRequests;
    private final ServerService serverService;
    private final ConnectedServersService connectedServersService;
    private final AuctionHouseUpdaterThread auctionHouseUpdaterThread;

    public BootData(
            ItemsService itemsService,
            ServerService serverService,
            BlizzardApiRequests blizzardApiRequests,
            ConnectedServersService connectedServersService,
            AuctionHouseUpdaterThread auctionHouseUpdaterThread) {
        this.itemsService = itemsService;
        this.serverService = serverService;
        this.blizzardApiRequests = blizzardApiRequests;
        this.connectedServersService = connectedServersService;
        this.auctionHouseUpdaterThread = auctionHouseUpdaterThread;
    }


    @Override
    public void run(String... args) throws Exception {

        this.innit();

        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(auctionHouseUpdaterThread, 0, 1, TimeUnit.HOURS);
    }

    private void innit() {

        if (serverService.getServers().size() == 0) {
            List<ConnectedServersModel> connectedServersModels = blizzardApiRequests.getListOfConnectedServers();
            connectedServersService.saveAll(connectedServersModels);
        }

        if (itemsService.count() == 0) {
            ItemEntity trueIronOre = new ItemEntity(109119L, "True Iron Ore");
            ItemEntity sumptuousFur = new ItemEntity(111557L, "Sumptuous Fur");
            ItemEntity goblinGliderKit = new ItemEntity(109076L, "Goblin Glider Kit");

            itemsService.saveAll(Arrays.asList(trueIronOre, sumptuousFur, goblinGliderKit));
        }


    }

}
