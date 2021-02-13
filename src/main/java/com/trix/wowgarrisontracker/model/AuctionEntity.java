package com.trix.wowgarrisontracker.model;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@Entity
public class AuctionEntity {

	@Id
	private Long id;
	private Long itemId;
	private Long unitPrice;
	private Long quantity;
	
//	@Override
//	public boolean equals(Object o) {
//		
//		if(o instanceof AuctionEntity) {
//			return this.id == ((AuctionEntity)o).getId();
//		}
//		return this.id == (Long)o;	
//	}
//	@Override
//	public int hashCode() {
//		return this.id.hashCode();
//	}
}
