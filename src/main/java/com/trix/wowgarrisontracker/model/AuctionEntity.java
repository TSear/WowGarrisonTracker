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
}
