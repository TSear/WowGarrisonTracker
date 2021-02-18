package com.trix.wowgarrisontracker.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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

    @Column(name = "login")
    private String login;

    @Column(name = "password")
    private String password;

	private Long garrisonResources;
	private Long warPaint;
	private Long amountOfEntries;
	
	public Account() {
		this.garrisonResources = 0l;
		this.warPaint = 0l;
		this.amountOfEntries = 0l;
	}
    //@OneToMany(mappedBy = "accountId")
    //private Set<AccountCharacter> accountCharacters;   

}
