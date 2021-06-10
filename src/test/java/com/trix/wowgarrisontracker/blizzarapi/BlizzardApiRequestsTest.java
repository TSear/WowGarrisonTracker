package com.trix.wowgarrisontracker.blizzarapi;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@ExtendWith(MockitoExtension.class)
@SpringBootTest
class BlizzardApiRequestsTest {

    @Autowired
    BlizzardApiRequests blizzardApiRequests;

    @Test
    void generateConnectedServerListRequestTest() {
        //given
        String region = "EU";

        String request = blizzardApiRequests.generateConnectedServerListRequest(BlizzardJWTToken.getToken(),"EU");

        //when
        List<String> lines = new ArrayList<>();
        try {
            Process process = Runtime.getRuntime().exec(request);
            lines = new BufferedReader(new InputStreamReader(process.getInputStream())).lines().collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }

        //then
        Assertions.assertNotEquals(0,lines.size());
    }

    @Test
    void connectedRealmsTest() {
        //given

        blizzardApiRequests.listOfConnectedRealms();
        //when

        //then
    }
}