package com.trix.wowgarrisontracker.wrapers;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.trix.wowgarrisontracker.model.ItemEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ItemsWraper {
	@JsonProperty("page")
	private int pageNumber;
	@JsonProperty("pageCount")
	private int pages;
	@JsonProperty("results")
	private List<ItemEntity> results;
	

}
