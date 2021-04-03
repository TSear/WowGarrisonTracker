package com.trix.wowgarrisontracker.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

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

    @OneToOne
    private Options options;

    @Column(name = "login")
    private String login;

    @Column(name = "password")
    private String password;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "account")
    private Set<AccountCharacter> accountCharacters;

    private Long amountOfEntries;


    public Account() {
        accountCharacters = new HashSet<>();
        this.amountOfEntries = 0L;
    }

    public int getAmountOfEntries(){
        return this.accountCharacters.stream()
                .map(accountCharacter -> accountCharacter.getEntries().size())
                .reduce(0, Integer::sum);
    }

    public Long getTotalGarrisonResources(){
        return this.accountCharacters.stream()
                .map(entry -> entry.getGarrisonResources())
                .reduce(0l, Long::sum);
    }

    public Long getTotalWarPaint(){
        return this.accountCharacters.stream()
                .map(entry -> entry.getWarPaint())
                .reduce(0l, Long::sum);
    }
}
