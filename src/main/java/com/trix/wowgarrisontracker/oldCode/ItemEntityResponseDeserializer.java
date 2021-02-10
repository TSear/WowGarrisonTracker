//package com.trix.wowgarrisontracker.oldCode;
//
//import java.io.IOException;
//
//import com.fasterxml.jackson.core.JsonParser;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.core.ObjectCodec;
//import com.fasterxml.jackson.databind.DeserializationContext;
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
//
//public class ItemEntityResponseDeserializer extends StdDeserializer<ItemEntity>{
//
//	public ItemEntityResponseDeserializer() {
//		this(null);
//	}
//	
//	protected ItemEntityResponseDeserializer(Class<?> vc) {
//		super(vc);
//	}
//
//	@Override
//	public ItemEntity deserialize(JsonParser p, DeserializationContext ctxt)
//			throws IOException, JsonProcessingException {
//
//		ItemEntity itemEntity = new ItemEntity();
//		ObjectCodec codec = p.getCodec();
//		JsonNode jsonNode = codec.readTree(p);
//		
//		JsonNode itemId = jsonNode.at("/data/id");
//		itemEntity.setId(itemId.asLong());
//		JsonNode itemName = jsonNode.at("/data/name/en_US");
//		itemEntity.setName(itemName.asText());
//		
//		
//		return itemEntity;
//	}
//	
//	
//
//}
