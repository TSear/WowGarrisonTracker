package com.trix.wowgarrisontracker.oldCode;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class ItemWrapperDeserializer extends StdDeserializer<ItemsWraper>{

	public ItemWrapperDeserializer(Class<?> vc) {
		super(vc);
	}
	
	public ItemWrapperDeserializer() {
		this(null);
	}

	@Override
	public ItemsWraper deserialize(JsonParser p, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		
		ItemsWraper itemsWraper = new ItemsWraper();
		ObjectCodec codec = p.getCodec();
		JsonNode jsonNode = codec.readTree(p);
		
		JsonNode pageCount = jsonNode.at("/pageCount");
		itemsWraper.setPages(pageCount.asInt());
		JsonNode page = jsonNode.at("/page");
		itemsWraper.setPageNumber(page.asInt());
		
		
		return itemsWraper;
	}

}
