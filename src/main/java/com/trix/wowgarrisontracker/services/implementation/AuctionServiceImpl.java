package com.trix.wowgarrisontracker.services.implementation;

import com.trix.wowgarrisontracker.blizzarapi.BlizzardApiRequests;
import com.trix.wowgarrisontracker.model.AuctionEntity;
import com.trix.wowgarrisontracker.model.ItemEntity;
import com.trix.wowgarrisontracker.pojos.AuctionPojo;
import com.trix.wowgarrisontracker.pojos.AuctionPojoWrapper;
import com.trix.wowgarrisontracker.repository.AuctionEntityRepository;
import com.trix.wowgarrisontracker.repository.ItemEntityRepository;
import com.trix.wowgarrisontracker.services.interfaces.AuctionService;
import com.trix.wowgarrisontracker.services.interfaces.ItemsService;
import com.trix.wowgarrisontracker.utils.BlizzardRequestUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AuctionServiceImpl implements AuctionService {

    private final ItemEntityRepository itemRepository;
    private final AuctionEntityRepository repository;
    private final BlizzardRequestUtils blizzardRequestUtils;
    private final ItemsService itemEntityService;
    private final BlizzardApiRequests blizzardApiRequests;


    public AuctionServiceImpl(ItemEntityRepository itemRepository, AuctionEntityRepository repository,
                              ItemsService itemEntityService, BlizzardRequestUtils blizzardRequestUtils,
                              BlizzardApiRequests blizzardApiRequests) {
        this.itemRepository = itemRepository;
        this.repository = repository;
        this.blizzardRequestUtils = blizzardRequestUtils;
        this.itemEntityService = itemEntityService;
        this.blizzardApiRequests = blizzardApiRequests;
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


    //TODO this is so awful. Need to read about multi threading and then redo this
    @Async
    @Override
    public void updateAuctionHouse() {

        while (true) {
            List<AuctionEntity> auctionEntities = blizzardApiRequests.getFilteredAuctions();
            this.removeAll();
            repository.saveAll(auctionEntities);
            System.out.println("Updated auctionHouse");
            try {
                Thread.sleep(30 * 60 * 1000);
//                Thread.sleep(1000*60);
                System.out.println("Updating auction House");
            } catch (InterruptedException e) {
                break;
            }

        }
    }

    @Override
    public List<AuctionPojoWrapper> findAllAuctions() {
        List<ItemEntity> itemEntities = itemEntityService.findAll();
        List<AuctionPojoWrapper> listOfAuctionPojos = new ArrayList<>();

        for (ItemEntity tmp : itemEntities) {
            AuctionPojoWrapper tmpAuctionPojo = new AuctionPojoWrapper();
            tmpAuctionPojo.setItemName(tmp.getName());

            List<AuctionPojo> converted = new ArrayList<>();
            for (AuctionEntity tmpAuctionEntity : getAuctionsByItemId(tmp.getId())) {
                AuctionPojo pojo = new AuctionPojo();
                pojo.setQuantity(tmpAuctionEntity.getQuantity());
                pojo.setUnitPrice(tmpAuctionEntity.getUnitPrice());
                if (converted.contains(pojo))
                    converted.get(converted.indexOf(pojo)).addQuantity(pojo.getQuantity());
                else
                    converted.add(pojo);
            }
            tmpAuctionPojo.setInfo(converted);
            listOfAuctionPojos.add(tmpAuctionPojo);
        }

        return listOfAuctionPojos;
    }
}
