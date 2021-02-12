package com.trix.wowgarrisontracker.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.trix.wowgarrisontracker.model.AuctionEntity;
import com.trix.wowgarrisontracker.pojos.AuctionHouseInfoPojo;
import com.trix.wowgarrisontracker.pojos.AuctionPojo;
import com.trix.wowgarrisontracker.services.interfaces.AuctionService;

@Component
public class AuctionToAuctionPojo implements Converter<AuctionEntity, AuctionPojo>{

	@Autowired
	private AuctionService auctionService;
	
	@Override
	public AuctionPojo convert(AuctionEntity source) {
		
		AuctionPojo pojo = new AuctionPojo();
//		AuctionHouseInfoPojo
//		pojo.setQuantity(source.getQuantity());
//		pojo.setUnitPrice(source.getUnitPrice());
//		pojo.setItemName(auctionService.getItemNameByAuction(source.getItemId()));
//		
		return pojo;
	}
	

}
