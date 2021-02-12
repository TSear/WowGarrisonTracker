package com.trix.wowgarrisontracker.pojos;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuctionPojo {

	private String itemName;
	private List<AuctionHouseInfoPojo> info;

	public AuctionPojo() {
		this.itemName = "";
		this.info = new ArrayList<>();
	}
	
}
