package com.trix.wowgarrisontracker.model;


import com.trix.wowgarrisontracker.pojos.Money;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDate;

@Setter
@Getter
@Table(name = "cardsOfOmen")
@Entity
public class CardsOfOmen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate localDate;

    private int amountOfCards;

    @Type(type = "com.trix.wowgarrisontracker.types.MoneyType")
    private Money moneyFromCards;

    @ManyToOne
    private Account account;

    public CardsOfOmen() {
        localDate = LocalDate.now();
        amountOfCards = 0;
        moneyFromCards = new Money();
    }

    public Money subtractMoney(Money money){
       return moneyFromCards.subtract(money);
    }
}
