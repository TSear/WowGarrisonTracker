package com.trix.wowgarrisontracker.pojos;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class AuctionPojoWrapper {

    private String itemName;
    private List<AuctionPojo> info;

    public AuctionPojoWrapper() {
        this.itemName = "";
        this.info = new ArrayList<>();
    }

}
