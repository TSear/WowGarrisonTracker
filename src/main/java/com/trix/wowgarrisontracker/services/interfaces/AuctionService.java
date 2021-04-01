package com.trix.wowgarrisontracker.services.interfaces;

import com.trix.wowgarrisontracker.model.AuctionEntity;
import com.trix.wowgarrisontracker.pojos.AuctionPojo;
import com.trix.wowgarrisontracker.pojos.AuctionPojoWrapper;

import java.util.List;

public interface AuctionService {

    boolean save(AuctionEntity auctionEntity);

    List<AuctionEntity> getAuctionsByItemId(Long itemId);

    String getItemNameByAuction(Long auctionItemId);

    void removeAll();

    void updateAuctionHouse();

    List<AuctionPojoWrapper> findAllAuctions();

}
