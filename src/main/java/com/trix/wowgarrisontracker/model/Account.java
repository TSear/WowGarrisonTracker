package com.trix.wowgarrisontracker.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.HashSet;
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

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "account", cascade = CascadeType.ALL)
    private Set<AccountCharacter> accountCharacters;

    private Long amountOfEntries;


    public Account() {
        accountCharacters = new HashSet<>();
        this.amountOfEntries = 0L;
        options = new Options(this);
    }

    public int getAmountOfEntries(){
        return this.accountCharacters.stream()
                .map(accountCharacter -> accountCharacter.getEntries().size())
                .reduce(0, Integer::sum);
    }

    public Long getTotalGarrisonResources(){
        return this.accountCharacters.stream()
                .map(AccountCharacter::getGarrisonResources)
                .reduce(0L, Long::sum);
    }

    public Long getTotalWarPaint(){
        return this.accountCharacters.stream()
                .map(AccountCharacter::getWarPaint)
                .reduce(0L, Long::sum);
    }
}
