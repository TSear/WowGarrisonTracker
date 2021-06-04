package com.trix.wowgarrisontracker.pojos;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class AuctionPojo {
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

    public void addQuantity(Long quantity) {
        this.quantity += quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuctionPojo that = (AuctionPojo) o;
        return unitPrice.equals(that.unitPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(unitPrice);
    }
}
