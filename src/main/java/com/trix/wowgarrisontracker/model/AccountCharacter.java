package com.trix.wowgarrisontracker.model;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Table(name = "accountCharacter")
@Entity
public class AccountCharacter {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String characterName;
    private Long totalCharacterGarrisonResources;
    private Long totalCharacterWarPaint;

    @ManyToOne
    @JoinColumn(name = "accountId", referencedColumnName = "id")
    private Account account;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "accountCharacter", cascade = CascadeType.ALL)
    private Set<Entry> entries;

    public AccountCharacter() {
        this.totalCharacterGarrisonResources = 0L;
        this.totalCharacterWarPaint = 0L;
        this.entries = new HashSet<>();
    }

    public void addNewEntry(Entry... entry) {
        for (Entry entry1 : entry) {
            totalCharacterGarrisonResources += entry1.getGarrisonResources();
            totalCharacterWarPaint += entry1.getWarPaint();
            entries.add(entry1);
        }
    }

    public boolean removeEntry(Entry entry){
        if(containsEntry(entry)){
            totalCharacterGarrisonResources -= entry.getGarrisonResources();
            totalCharacterWarPaint -= entry.getWarPaint();
            entries.remove(entry);
            return true;
        }
        return false;
    }

    public boolean containsEntry(Entry entry){
        return entries.contains(entry);
    }

    public int getAmountOfEntries(){
        return entries.size();
    }


    public Long getGarrisonResources() {
        return (long) (entries.stream().map(Entry::getGarrisonResources).reduce(0, Integer::sum));
    }

    public Long getWarPaint() {
        return (long) (entries.stream().map(Entry::getWarPaint).reduce(0, Integer::sum));
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public void setCharacterName(String characterName) {
        this.characterName = characterName;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTotalGarrisonResorces(){
        return this.totalCharacterGarrisonResources;
    }

    public Long getTotalWarPaint(){
        return this.totalCharacterWarPaint;
    }

    public Long getId(){
        return this.id;
    }

    public String getCharacterName(){
        return this.characterName;
    }

    public Collection<Entry> getEntries(){
        return this.entries;
    }

    public Account getAccount(){
        return this.account;
    }
}