package com.trix.wowgarrisontracker.model;

import java.time.LocalDate;

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

    public Long getUpdatedAccountCharacterGarrisonResources() {
        if (this.getAccountCharacter() == null)
            throw new RuntimeException("Entry does not contain account character info");
        return this.getGarrisonResources() + this.getAccountCharacter().getGarrisonResources();
    }

    public Long getUpdatedAccountCharacterWarPaint() {
        if (this.getAccountCharacter() == null)
            throw new RuntimeException("Entry does not contain account character info");
        return this.warPaint + this.getAccountCharacter().getWarPaint();
    }
}
