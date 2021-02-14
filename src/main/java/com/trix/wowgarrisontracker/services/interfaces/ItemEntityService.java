package com.trix.wowgarrisontracker.services.interfaces;

import java.util.List;

import com.trix.wowgarrisontracker.model.ItemEntity;

public interface ItemEntityService{

	boolean save(ItemEntity itemEntity);
	
	List<ItemEntity> findAllItemEntities();
	
}
