package com.trix.wowgarrisontracker.blizzarapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trix.wowgarrisontracker.enums.Regions;
import com.trix.wowgarrisontracker.model.Server;
import com.trix.wowgarrisontracker.services.interfaces.ItemsService;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class BlizzardApiRequests {


    private ItemsService itemsService;


    public BlizzardApiRequests(ItemsService itemsService) {
        this.itemsService = itemsService;
    }

    public String executeApiRequest(String curlRequest) {
        String jsonData = "";
        try {
            Process process = Runtime.getRuntime().exec(curlRequest);
            InputStream inputStream = process.getInputStream();
            jsonData = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8)).lines()
                    .collect(Collectors.joining("\n"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return jsonData;
    }


    public List<Integer> listOfConnectedRealms() {

        ObjectMapper mapper = new ObjectMapper();

        String token = BlizzardJWTToken.getToken();

        List<String> regions = Arrays.stream(Regions.values())
                .skip(1)
                .map(Regions::getValue)
                .collect(Collectors.toList());

        List<Integer> listOfConnectedRealmsId = new ArrayList<>();

        for (String region : regions) {

            String request = generateConnectedServerListRequest(token, region);

            String connectedRealmJasonData = executeApiRequest(request);

            JsonNode parent;

            try {
                parent = mapper.readTree(connectedRealmJasonData);
                JsonNode realmsJson = parent.path("connected_realms");
                ConnectedServerModel [] convertedLines = mapper.convertValue(realmsJson,ConnectedServerModel[].class);
                listOfConnectedRealmsId.addAll(Arrays.stream(convertedLines)
                        .map(ConnectedServerModel::extractId)
                        .collect(Collectors.toList()));

                System.out.println();
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

        }

        return new ArrayList<>();
    }


//    public List<AuctionEntity> getAuctionHouse() {
//
//        ObjectMapper auctionMapper = new ObjectMapper();
//        List<AuctionEntity> listOfParsedAuctionEntities = new ArrayList<>();
//
//        SimpleModule module = new SimpleModule("AuctionHouseResponseDeserializer",
//                new Version(1, 0, 0, null, null, null));
//        module.addDeserializer(AuctionEntity.class, new AuctionHouseResponseDeserializer());
//
//        auctionMapper.registerModule(module);
//        auctionMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//
//        String stringJsonData = executeApiRequest("curl -X GET https://eu.api.blizzard.com/data/wow/connected-realm/3713/auctions?"
//                + "namespace=dynamic-eu&locale=en_US&access_token=" + getTokenKey());
//
//        try {
//            JsonNode jsonWithAllInformation = auctionMapper.readTree(stringJsonData);
//
//            JsonNode jsonWithAuctions = jsonWithAllInformation.path("auctions");
//            listOfParsedAuctionEntities = auctionMapper.convertValue(jsonWithAuctions, new TypeReference<List<AuctionEntity>>() {
//            });
//
//        } catch (JsonProcessingException e) {
//            //TODO exception handling
//            e.printStackTrace();
//
//        }
//
//        return listOfParsedAuctionEntities;
//
//    }


    public List<Server> getListOfServers() {

        String token = BlizzardJWTToken.getToken();

        ObjectMapper mapper = new ObjectMapper();

        //First region is 'all' so I can't make request for it
        List<String> regionsToRequest = Arrays.stream(Regions.values())
                .skip(1)
                .map(Regions::getValue)
                .collect(Collectors.toList());

        List<Server> listOfServers = new ArrayList<>();

        for (String region : regionsToRequest) {
            listOfServers.addAll(parseJsonServerList(region, mapper, token));
        }

        return listOfServers;
    }

    private List<Server> parseJsonServerList(String region, ObjectMapper mapper, String token) {

        String jsonData;

        String curlRequest = generateServerListRequest(token, region);

        jsonData = executeApiRequest(curlRequest);

        JsonNode parent;

        try {

            parent = mapper.readTree(jsonData);
            JsonNode realms = parent.path("realms");
            Server[] serverValues = mapper.convertValue(realms, Server[].class);
            Arrays.stream(serverValues).forEach(server -> server.setRegion(region));

            return Arrays.asList(serverValues);

        } catch (JsonProcessingException e) {
            //TODO exception handling
            e.printStackTrace();
        }

        return new ArrayList<>();

    }


    private String generateServerListRequest(String token, String region) {
        return "curl " +
                "-X GET https://" +
                region.toLowerCase() +
                ".api.blizzard.com/data/wow/realm/index?namespace=dynamic-" +
                region +
                "&locale=en_US&access_token=" +
                token;
    }

    public String generateConnectedServerListRequest(String token, String region) {
        return "curl " +
                "-X GET https://" +
                region.toLowerCase() +
                ".api.blizzard.com/data/wow/connected-realm/index?namespace=dynamic-" +
                region.toLowerCase() +
                "&locale=en_US&access_token=" +
                token;
    }
}