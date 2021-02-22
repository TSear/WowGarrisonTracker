package com.trix.wowgarrisontracker.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@NoArgsConstructor
@Setter
@Getter
@Entity
public class AccountCharacter {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String characterName;
//
//	@Column(name = "accountId")
//	private Long accountId;
	
	@ManyToOne
    @JoinColumn(name="accountId", referencedColumnName = "id")
	private Account account;

	@OneToMany(mappedBy = "accountCharacter")
	private Set<Entry> entries;

}