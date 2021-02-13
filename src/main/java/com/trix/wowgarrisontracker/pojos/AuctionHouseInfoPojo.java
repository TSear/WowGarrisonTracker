package com.trix.wowgarrisontracker.pojos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuctionHouseInfoPojo {
	private Long unitPrice;
	private Long quantity;

	public String getPrice() {
		String result = "";
		Long price = this.unitPrice;

		result = price % 100 + "c";
		price /= 100;
		result = price % 100 + "s " + result;
		price /= 100;
		result = price + "g " + result;
		

		return result;
	}
}
