package com.trix.wowgarrisontracker.model;

import java.util.Set;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter

@Entity
public class AccountCharacter {


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String characterName;
	private Long garrisonResources;
	private Long warPaint;

	@ManyToOne
    @JoinColumn(name="accountId", referencedColumnName = "id")
	private Account account;

	@OneToMany(mappedBy = "accountCharacter")
	private Set<Entry> entries;

	public AccountCharacter() {
		this.garrisonResources = 0l;
		this.warPaint = 0l;
	}
}