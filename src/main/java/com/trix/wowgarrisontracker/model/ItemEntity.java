package com.trix.wowgarrisontracker.model;

import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
public class ItemEntity {


	@Id
	private Long id;
	private String name;
	
	@JsonProperty("data")
    private void unpackNested(Map<String,Object> brand) {

        this.id = Long.valueOf(""+brand.get("id"));
        Map<String,String> owner = (Map<String,String>)brand.get("name");
        this.name = owner.get("en_US");
    }
}
