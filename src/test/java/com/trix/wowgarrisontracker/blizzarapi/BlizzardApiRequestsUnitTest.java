package com.trix.wowgarrisontracker.blizzarapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.trix.wowgarrisontracker.model.AuctionEntity;
import com.trix.wowgarrisontracker.services.interfaces.ConnectedServersService;
import com.trix.wowgarrisontracker.services.interfaces.ServerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BlizzardApiRequestsUnitTest {

    String auctionJsonPath = "src/test/java/com/trix/wowgarrisontracker/blizzarapi/exampleJson/auctionsJson.json5";


    @Mock
    ServerService serverService;

    @Mock
    ConnectedServersService connetctedServersService;

    BlizzardJWTToken blizzardJWTToken;

    @InjectMocks
    BlizzardApiRequests blizzardApiRequests;

    @BeforeEach
    void setUp() {

    }

    @Test
    void executeApiRequest_noToken() {
        //given
        String request = "curl -i -X GET https://us.api.blizzard.com/data/wow/connected-realm/index?namespace=dynamic-us&locale=en_US";

        //when
        String result = blizzardApiRequests.executeApiRequest(request);
        String [] resultArray = result.split(" ");

        //then
        assertEquals("401",resultArray[1]);
    }

    @Test
    void executeApiRequest_correct() {
        //given

        //when

        //then
    }

    @Test
    void parseAuctionsJson_successfully() throws IOException {
        //given
        String json = new String(Files.readAllBytes(Paths.get(auctionJsonPath)));

        //when
        List<AuctionEntity> auctions = blizzardApiRequests.parseAuctionsJson(json);

        //then
        assertTrue(auctions.size()>0);
    }

    @Test
    void parseAuctionsJson_exception() {
        //given
        String wrongJson = "{";
        //when

        //then
        assertThrows(JsonProcessingException.class,()->blizzardApiRequests.parseAuctionsJson(wrongJson));
    }

    @Test
    void getAuctions_successfully() {
        //given

        //when

        //then
    }

    @Test
    void getFilteredAuctions_Successfully() {
        //given

        //when

        //then
    }
}














