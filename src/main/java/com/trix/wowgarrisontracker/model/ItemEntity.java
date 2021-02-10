package com.trix.wowgarrisontracker.model;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class ItemEntity {

	@Id
	private Long id;
	private String name;
	
}
