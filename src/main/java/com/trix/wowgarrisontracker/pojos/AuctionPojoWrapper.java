package com.trix.wowgarrisontracker.pojos;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

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
