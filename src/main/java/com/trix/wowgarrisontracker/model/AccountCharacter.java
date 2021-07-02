package com.trix.wowgarrisontracker.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Table(name = "accountCharacter")
@Entity
public class AccountCharacter {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String characterName;

    @ManyToOne
    @JoinColumn(name = "accountId", referencedColumnName = "id")
    private Account account;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "accountCharacter", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private final Set<Entry> entries;




    public AccountCharacter() {
        this.entries = new HashSet<>();
    }

    public boolean containsEntry(Entry entry) {
        return entries.contains(entry);
    }

    public int getAmountOfEntries() {
        return entries.size();
    }

    public Long getGarrisonResources() {
        return (long) (entries.stream().map(Entry::getGarrisonResources).reduce(0, Integer::sum));
    }

    public Long getWarPaint() {
        return (long) (entries.stream().map(Entry::getWarPaint).reduce(0, Integer::sum));
    }


}