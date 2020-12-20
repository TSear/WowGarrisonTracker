package com.trix.wowgarrisontracker.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
@Table(name = "character")
@Entity
public class AccountCharacter{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "characterName")
    private String characterName;

    @Column(name = "accountId")
    private Long accountId;

    @OneToMany(mappedBy = "accountCharacterId")
    private Set<Entry> entries;  

}