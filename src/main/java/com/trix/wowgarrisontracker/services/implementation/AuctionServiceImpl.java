package com.trix.wowgarrisontracker.services.implementation;

import java.util.List;
import java.util.Optional;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.trix.wowgarrisontracker.model.AuctionEntity;
import com.trix.wowgarrisontracker.model.Auctions;
import com.trix.wowgarrisontracker.model.ItemEntity;
import com.trix.wowgarrisontracker.repository.AuctionEntityRepository;
import com.trix.wowgarrisontracker.repository.ItemEntityRepository;
import com.trix.wowgarrisontracker.services.interfaces.AuctionService;
import com.trix.wowgarrisontracker.services.interfaces.ItemEntityService;
import com.trix.wowgarrisontracker.utils.BlizzardRequestUtils;

import lombok.extern.slf4j.Slf4j;

@Service
public class AuctionServiceImpl implements AuctionService {

    private ItemEntityRepository itemRepository;
    private AuctionEntityRepository repository;
    private BlizzardRequestUtils blizzardRequestUtils;
    private Logger logger = LoggerFactory.getLogger(Slf4j.class);
    private ItemEntityService itemEntityService;


    public AuctionServiceImpl(ItemEntityRepository itemRepository, AuctionEntityRepository repository,
                              ItemEntityService itemEntityService) {
        this.itemRepository = itemRepository;
        this.repository = repository;
        this.blizzardRequestUtils = new BlizzardRequestUtils();
        this.itemEntityService = itemEntityService;
    }


    @Override
    public boolean save(AuctionEntity auctionEntity) {
        return repository.save(auctionEntity) != null;
    }


    @Override
    public List<AuctionEntity> getAuctionsByItemId(Long itemId) {

        return repository.findAuctionEntityByItemId(itemId);
    }


    @Override
    public String getItemNameByAuction(Long auctionItemId) {

        Optional<ItemEntity> optionalItem = itemRepository.findById(auctionItemId);
        if (optionalItem.isPresent()) {
            return optionalItem.get().getName();
        }

        return "";
    }

    @Override
    public void removeAll() {
        repository.deleteAll();
    }


    @Async
    @Override
    public void updateAuctionHouse() {

        while (true) {

            Auctions auctions = blizzardRequestUtils.getAuctionHouse();
            List<ItemEntity> items = itemEntityService.findAllItemEntities();
            this.removeAll();
            auctions.getAuctions().stream().filter(x -> items.contains(new ItemEntity(x.getItemId()))).forEach(this::save);
            logger.info("Updating auction house");
            try {
                Thread.sleep(30 * 60 * 1000);
            } catch (InterruptedException e) {
                logger.error("Interrupted thread sleep");
                break;
            }

        }
    }


}
