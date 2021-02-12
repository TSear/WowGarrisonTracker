package com.trix.wowgarrisontracker.services.interfaces;

import java.util.List;

import com.trix.wowgarrisontracker.model.AuctionEntity;

public interface AuctionService {

	boolean save(AuctionEntity auctionEntity);
	
	List<AuctionEntity> getAuctionsByItemId(Long itemId);
	
	String getItemNameByAuction(Long auctionItemId);
	
	void removeAll();
	
}
