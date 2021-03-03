package com.trix.wowgarrisontracker.model;

import java.util.Set;

import javax.persistence.*;

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