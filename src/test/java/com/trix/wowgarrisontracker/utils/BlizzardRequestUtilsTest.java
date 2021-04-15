package com.trix.wowgarrisontracker.utils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;


//To run this test you need to add client.id and client.secret to environmental variables

@ExtendWith(MockitoExtension.class)
public class BlizzardRequestUtilsTest {

    String clientId;
    String clientSecret;
    @Spy
    BlizzardRequestUtils blizzardRequestUtils;

    @BeforeEach
    void setUp() {
        clientId = System.getenv("client.id");
        clientSecret = System.getenv("client.secret");
        blizzardRequestUtils = Mockito.spy(new BlizzardRequestUtils());
        blizzardRequestUtils.setClient_id(clientId);
        blizzardRequestUtils.setClient_secret(clientSecret);
    }

    @Test
    void getToken_success() {
        //given

        //when
        try {
            blizzardRequestUtils.getAccessToken();
        } catch (JsonProcessingException e) {
            fail();
        }

        //then
        assertNotNull(blizzardRequestUtils.getToken());
        assertEquals("bearer ", blizzardRequestUtils.getToken().substring(0, 7));

    }

    @Test
    void getToken_fail() {
        //given
        blizzardRequestUtils.setClient_secret("0");

        //when
        try {
            blizzardRequestUtils.getAccessToken();
        } catch (JsonProcessingException e) {
            fail();
        }

        //then
        assertTrue(blizzardRequestUtils.getToken().length() < 7);
        assertFalse(blizzardRequestUtils.getToken().startsWith("bearer "));

    }

    @Test
    void isTokenValid_true() {
        //given

        //when
        try {
            blizzardRequestUtils.getAccessToken();
        } catch (JsonProcessingException e) {
            fail();
        }

        //then
        assertTrue(BlizzardRequestUtils.isTokenValid(BlizzardRequestUtils.getExpirationTime()));
    }

    @Test
    void isTokenValid_incorrectToken() {
        //give

        //when
        blizzardRequestUtils.setClient_id("00");
        try {
            blizzardRequestUtils.getAccessToken();
        } catch (JsonProcessingException e) {
            fail();
        }

        //then
        assertFalse(BlizzardRequestUtils.isTokenValid(BlizzardRequestUtils.getExpirationTime()));
    }

    @Test
    void isTokenValid_outdated() {
        //given

        //when
        try {
            blizzardRequestUtils.getAccessToken();
        } catch (JsonProcessingException e) {
            fail();
        }

        //then
        assertFalse(BlizzardRequestUtils.isTokenValid(LocalDateTime.now().minusHours(1)));

    }

    @Test
    void isTokenValid_nullTime() {
        //given

        //when

        //then
        assertFalse(BlizzardRequestUtils.isTokenValid(null));
    }

    @Test
    public void getAccessToken_failing() {
        //given

        //when
        when(blizzardRequestUtils.getJsonData(Mockito.anyString())).thenReturn("{l}");

        //then
        assertThrows(JsonParseException.class, () -> blizzardRequestUtils.getAccessToken());

    }

    @Test
    void getAccessToken_successfully() {
        //given

        //when
        when(blizzardRequestUtils.getJsonData(Mockito.anyString())).thenReturn("{\"access_token\":\"USaXbMW9q8Ha0977AD55Zw0Cff8gYqueAd\",\"token_type\":\"bearer\",\"expires_in\":86399,\"sub\":\"b0ea73a8d8f74e9b949e5c9868911b7f\"}");
        try {
            blizzardRequestUtils.getAccessToken();
        } catch (JsonProcessingException e) {
            fail("Failed during generating token -> getAccessToken_successfully");
        }
        //then
        assertTrue(blizzardRequestUtils.getToken() != null);
        assertTrue(blizzardRequestUtils.getToken().startsWith("bearer "));
        assertTrue(blizzardRequestUtils.getToken().length() > 7);

    }

    @Test
    void auctionHouse_successfully() {
        //given

        //when
        try {
            blizzardRequestUtils.getAccessToken();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        //then
        blizzardRequestUtils.getAuctionHouse();
    }
}
