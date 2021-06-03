package com.trix.wowgarrisontracker.model;

import java.time.LocalDate;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@Table(name = "entry")
@Entity
public class Entry {

    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
//
//    private Long accountCharacterId;
//
    //@DateTimeFormat
    @Column(name = "entryDate")
    private LocalDate entryDate;

    @Column(name = "garrisonResources", columnDefinition = "int default 0")
    private int garrisonResources;

    @Column(name = "warPaint", columnDefinition = "int default 0")
    private int warPaint;

    @ManyToOne
	@JoinColumn(name = "accountCharacterId", referencedColumnName = "id")
    private AccountCharacter accountCharacter;
   
    public Entry(){
        this.entryDate = LocalDate.now();
        this.garrisonResources = 0;
        this.warPaint = 0;
    }

    public void setWarPaintToNegative() {
        this.setWarPaint(-this.getWarPaint());
    }

    public void setGarrisonResourcesToNegative() {
        this.setGarrisonResources(-this.getGarrisonResources());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Entry entry = (Entry) o;
        return garrisonResources == entry.garrisonResources && warPaint == entry.warPaint && id.equals(entry.id) && Objects.equals(entryDate, entry.entryDate) && Objects.equals(accountCharacter, entry.accountCharacter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, entryDate, garrisonResources, warPaint, accountCharacter);
    }
}
