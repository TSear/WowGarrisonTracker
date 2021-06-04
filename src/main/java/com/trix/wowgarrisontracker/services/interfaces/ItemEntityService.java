package com.trix.wowgarrisontracker.services.interfaces;

import com.trix.wowgarrisontracker.model.ItemEntity;

import java.util.List;

public interface ItemEntityService {

    boolean save(ItemEntity itemEntity);

    List<ItemEntity> findAllItemEntities();

}
