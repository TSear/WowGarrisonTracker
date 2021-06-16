package com.trix.wowgarrisontracker.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
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

    @ManyToOne
    private ItemEntity itemEntity;

    public AuctionEntity(Long id, Long itemId, Long unitPrice, Long quantity, Integer connectedServerId) {
        this.id = id;
        this.itemId = itemId;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.connectedServerId = connectedServerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuctionEntity that = (AuctionEntity) o;
        return itemId.equals(that.itemId) && unitPrice.equals(that.unitPrice) && connectedServerId.equals(that.connectedServerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemId, unitPrice, connectedServerId);
    }
}
