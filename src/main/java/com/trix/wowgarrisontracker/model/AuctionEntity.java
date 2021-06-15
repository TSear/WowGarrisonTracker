package com.trix.wowgarrisontracker.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@ToString
@Getter
@Setter
@Table(name = "auctionEntity")
@Entity
public class AuctionEntity {

    @Id
    private Long id;
    private Long itemId;
    private Long unitPrice;
    private Long quantity;
    private Integer connectedServerId;


}
