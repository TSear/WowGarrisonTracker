package com.trix.wowgarrisontracker.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.trix.wowgarrisontracker.model.AuctionEntity;
import com.trix.wowgarrisontracker.pojos.AuctionPojoWrapper;
import com.trix.wowgarrisontracker.services.interfaces.AuctionService;

@Component
public class AuctionToAuctionPojo implements Converter<AuctionEntity, AuctionPojoWrapper>{

	@Autowired
	private AuctionService auctionService;
	
	@Override
	public AuctionPojoWrapper convert(AuctionEntity source) {
		
		AuctionPojoWrapper pojo = new AuctionPojoWrapper();

		return pojo;
	}
	

}
