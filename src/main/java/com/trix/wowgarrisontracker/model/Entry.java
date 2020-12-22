package com.trix.wowgarrisontracker.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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

    @ManyToOne
    private AccountCharacter accountCharacterId;

    //@DateTimeFormat
    @Column(name = "entryDate")
    private LocalDate entryDate;

    @Column(name = "garrisonResources")
    private int garrisonResources;

    @Column(name = "warPaint")
    private int warPaint;

    public Entry(){
        this.entryDate = LocalDate.now();
        this.garrisonResources = 0;
        this.warPaint = 0;
    }


}
