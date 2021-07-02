package com.trix.wowgarrisontracker.blizzarapi;

import com.trix.wowgarrisontracker.model.AuctionEntity;
import com.trix.wowgarrisontracker.services.interfaces.AuctionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@Scope("prototype")
public class AuctionHouseUpdaterThread implements Runnable{

    private final BlizzardApiRequests blizzardApiRequests;
    private final AuctionService auctionService;

    public AuctionHouseUpdaterThread(BlizzardApiRequests blizzardApiRequests, AuctionService auctionService) {
        this.blizzardApiRequests = blizzardApiRequests;
        this.auctionService = auctionService;
    }

    @Override
    public void run() {
        update();
    }

    private void update(){
        List<AuctionEntity> auctionEntities = blizzardApiRequests.getFilteredAuctions();
        auctionService.removeAll();
        auctionService.saveAll(auctionEntities);
        log.info("Updated auction house");
    }
}
