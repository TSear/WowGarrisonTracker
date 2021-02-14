package com.trix.wowgarrisontracker.oldCode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;


public class ItemEntityOld {


	private Long id;
	private String name;
	
	@JsonProperty("data")
    private void unpackNested(Map<String,Object> brand) {
		List<Long> listOfLongs = new ArrayList<>();
		listOfLongs.addAll((Arrays.asList(1l,2l,3l,4l,5l,6l)));
		listOfLongs.stream().forEach(System.out::println);
        this.id = Long.valueOf(""+brand.get("id"));
        Map<String,String> owner = (Map<String,String>)brand.get("name");
        this.name = owner.get("en_US");
    }
}
