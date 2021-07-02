package com.trix.wowgarrisontracker.services.interfaces;

import com.trix.wowgarrisontracker.model.AuctionEntity;
import com.trix.wowgarrisontracker.model.ItemEntity;

import java.util.Collection;
import java.util.List;

public interface ItemsService {

    boolean save(ItemEntity itemEntity);

    List<ItemEntity> findAll();

    int count();

    void saveAll(List<ItemEntity> itemEntities);
}
