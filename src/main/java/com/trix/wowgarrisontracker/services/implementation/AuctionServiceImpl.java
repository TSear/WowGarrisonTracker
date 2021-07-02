package com.trix.wowgarrisontracker.services.implementation;

import com.trix.wowgarrisontracker.blizzarapi.BlizzardApiRequests;
import com.trix.wowgarrisontracker.converters.AuctionToAuctionPojo;
import com.trix.wowgarrisontracker.model.AuctionEntity;
import com.trix.wowgarrisontracker.model.ItemEntity;
import com.trix.wowgarrisontracker.pojos.AuctionPojo;
import com.trix.wowgarrisontracker.pojos.AuctionPojoWrapper;
import com.trix.wowgarrisontracker.repository.AuctionEntityRepository;
import com.trix.wowgarrisontracker.repository.ItemEntityRepository;
import com.trix.wowgarrisontracker.services.interfaces.AuctionService;
import com.trix.wowgarrisontracker.services.interfaces.ItemsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AuctionServiceImpl implements AuctionService {

    private final ItemEntityRepository itemRepository;
    private final AuctionEntityRepository repository;
    private final ItemsService itemEntityService;
    private final BlizzardApiRequests blizzardApiRequests;
    private final AuctionToAuctionPojo auctionPojo;


    public AuctionServiceImpl(ItemEntityRepository itemRepository, AuctionEntityRepository repository,
                              ItemsService itemEntityService,
                              BlizzardApiRequests blizzardApiRequests) {
        this.itemRepository = itemRepository;
        this.repository = repository;
        this.itemEntityService = itemEntityService;
        this.blizzardApiRequests = blizzardApiRequests;
        auctionPojo = new AuctionToAuctionPojo();
    }


    @Override
    public boolean save(AuctionEntity auctionEntity) {
        repository.save(auctionEntity);
        return true;
    }

    @Override
    public boolean saveAll(List<AuctionEntity> auctionEntities) {
        if (auctionEntities != null) {
            repository.saveAll(auctionEntities);
            return true;
        }
        return false;
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

    @Override
    public List<AuctionEntity> findByConnectedServerId(Integer connectedServerId) {

        return repository.findByConnectedServerId(connectedServerId);

    }

    @Override
    public List<AuctionPojoWrapper> findByConnectedServerIdPojo(Integer connectedServerId) {

        List<AuctionPojo> collect = findByConnectedServerId(connectedServerId).stream().map(auctionPojo::convert).collect(Collectors.toList());
        return flattenAuctions(collect);
    }

    private List<AuctionPojoWrapper> flattenAuctions(List<AuctionPojo> auctions) {

        List<ItemEntity> items = itemEntityService.findAll();

        Map<Long, List<AuctionPojo>> groupedByItemId = auctions.stream().collect(Collectors.groupingBy(AuctionPojo::getItemId));

        return groupedByItemId.entrySet().stream()
                .map(entry -> convertToWrapper(entry.getKey(), entry.getValue(), items))
                .collect(Collectors.toList());
    }

    private AuctionPojoWrapper convertToWrapper(Long id, List<AuctionPojo> auctions, List<ItemEntity> items) {

        AuctionPojoWrapper auctionPojoWrapper = new AuctionPojoWrapper(id, auctions);

        if (auctions.size() > 0)
            auctionPojoWrapper.setItemName(auctions.get(0).getItemName());

        auctionPojoWrapper.flattenAuctions();
        return auctionPojoWrapper;

    }

}
