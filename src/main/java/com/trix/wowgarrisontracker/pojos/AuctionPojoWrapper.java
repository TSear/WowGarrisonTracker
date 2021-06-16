package com.trix.wowgarrisontracker.pojos;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
public class AuctionPojoWrapper {

    private String itemName;
    private Long itemId;
    private List<AuctionPojo> info;

    public AuctionPojoWrapper() {
        this.itemName = "";
        this.info = new ArrayList<>();
    }

    public AuctionPojoWrapper(Long itemId, List<AuctionPojo> info) {
        this.itemId = itemId;
        this.info = info;
    }

    public void flattenAuctions() {

        List<AuctionPojo> flattenedList = new ArrayList<>();
        for (AuctionPojo auctionPojo : info) {
           if(flattenedList.contains(auctionPojo)){
               flattenedList.get(flattenedList.indexOf(auctionPojo)).addQuantity(auctionPojo.getQuantity());
           }else{
               flattenedList.add(auctionPojo);
           }
        }
        info = flattenedList;
    }
}
