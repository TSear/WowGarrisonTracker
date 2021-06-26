package com.trix.wowgarrisontracker.model;

import com.trix.wowgarrisontracker.pojos.Money;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@ToString
@Getter
@Setter
@Table(name = "account")
@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL)
    private Options options;

    @Column(name = "login")
    private String login;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "account", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<AccountCharacter> accountCharacters;

    private Long amountOfEntries;

    private Long amountOfOpenedCards;

    @Column(name = "totalGoldFromCards")
    @Type(type = "com.trix.wowgarrisontracker.types.MoneyType")
    private Money totalGoldFromCards;

    @Column(length = 64)
    private String verificationCode;

    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<CardsOfOmen> cardsOfOmenList;

    @Column(name = "enabled")
    private boolean enabled;


    public Account() {
        amountOfOpenedCards = 0L;
        totalGoldFromCards = new Money();
        accountCharacters = new HashSet<>();
        this.amountOfEntries = 0L;
        options = new Options(this);
    }

    public void addCards(CardsOfOmen cards){
        this.amountOfOpenedCards += cards.getAmountOfCards();
        this.totalGoldFromCards.addMoney(cards.getMoneyFromCards());
    }

    public void removeCards(CardsOfOmen cards){
        this.amountOfOpenedCards -= cards.getAmountOfCards();
        if(this.amountOfOpenedCards < 0)
            this.amountOfOpenedCards = 0L;
        this.totalGoldFromCards.subtractMoney(cards.getMoneyFromCards());
        if(totalGoldFromCards.getCopperValue() <0)
            this.totalGoldFromCards.setCopper(0L);
    }

    public int getAmountOfEntries() {
        return this.accountCharacters.stream()
                .map(AccountCharacter::getAmountOfEntries)
                .reduce(0, Integer::sum);
    }

    public Long getTotalGarrisonResources() {
        return this.accountCharacters.stream()
                .map(AccountCharacter::getTotalGarrisonResources)
                .reduce(0L, Long::sum);
    }

    public Long getTotalWarPaint() {
        return this.accountCharacters.stream()
                .map(AccountCharacter::getTotalWarPaint)
                .reduce(0L, Long::sum);
    }

    public void addOpenedCards(CardsOfOmen cards){
        this.amountOfOpenedCards += cards.getAmountOfCards();
        totalGoldFromCards.addCopper(cards.getMoneyFromCards().getCopperValue());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return id.equals(account.id) && login.equals(account.login) && password.equals(account.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, login, password);
    }
}
