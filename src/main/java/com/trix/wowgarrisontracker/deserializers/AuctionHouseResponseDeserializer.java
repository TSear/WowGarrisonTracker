package com.trix.wowgarrisontracker.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.trix.wowgarrisontracker.model.AuctionEntity;

import java.io.IOException;

public class AuctionHouseResponseDeserializer extends StdDeserializer<AuctionEntity> {

    public AuctionHouseResponseDeserializer() {
        this(null);
    }

    public AuctionHouseResponseDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public AuctionEntity deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException {

        AuctionEntity auctionEntity = new AuctionEntity();
        ObjectCodec objectCodec = p.getCodec();
        JsonNode jsonNode = objectCodec.readTree(p);

        JsonNode id = jsonNode.at("/id");
        auctionEntity.setId(id.asLong());
        JsonNode itemId = jsonNode.at("/item/id");
        auctionEntity.setItemId(itemId.asLong());
        JsonNode quantity = jsonNode.at("/quantity");
        auctionEntity.setQuantity(quantity.asLong());
        JsonNode unitPrice = jsonNode.at("/unit_price");
        auctionEntity.setUnitPrice(unitPrice.asLong());
        return auctionEntity;
    }

}
