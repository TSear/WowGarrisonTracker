package com.trix.wowgarrisontracker.pojos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CardsOfOmenEntryPojo {

    private Integer amountOfCards;
    private Money startingMoney;
    private Money endingMoney;

    public CardsOfOmenEntryPojo() {
        amountOfCards = 0;
        startingMoney = new Money();
        endingMoney = new Money();
    }
}
