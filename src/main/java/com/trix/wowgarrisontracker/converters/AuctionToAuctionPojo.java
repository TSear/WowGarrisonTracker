package com.trix.wowgarrisontracker.converters;

import com.trix.wowgarrisontracker.model.AuctionEntity;
import com.trix.wowgarrisontracker.pojos.AuctionPojo;
import com.trix.wowgarrisontracker.pojos.AuctionPojoWrapper;
import com.trix.wowgarrisontracker.services.interfaces.AuctionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class AuctionToAuctionPojo implements Converter<AuctionEntity, AuctionPojo> {

    @Override
    public AuctionPojo convert(AuctionEntity auctionEntity) {

        AuctionPojo auctionPojo = new AuctionPojo();
        auctionPojo.setQuantity(auctionEntity.getQuantity());
        auctionPojo.setUnitPrice(auctionEntity.getUnitPrice());
        auctionPojo.setItemId(auctionEntity.getItemId());
        auctionPojo.setItemName(auctionEntity.getItemEntity().getName());

        return auctionPojo;
    }
}
