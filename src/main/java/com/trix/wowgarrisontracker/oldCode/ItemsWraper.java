package com.trix.wowgarrisontracker.oldCode;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

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
	private List<ItemEntityOld> results;
	

}
