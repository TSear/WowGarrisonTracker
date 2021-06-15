package com.trix.wowgarrisontracker.blizzarapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.trix.wowgarrisontracker.deserializers.AuctionHouseResponseDeserializer;
import com.trix.wowgarrisontracker.enums.Regions;
import com.trix.wowgarrisontracker.model.AuctionEntity;
import com.trix.wowgarrisontracker.model.ConnectedServersModel;
import com.trix.wowgarrisontracker.model.ItemEntity;
import com.trix.wowgarrisontracker.model.Server;
import com.trix.wowgarrisontracker.services.interfaces.ConnectedServersService;
import com.trix.wowgarrisontracker.services.interfaces.ItemsService;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class BlizzardApiRequests {


    private final ItemsService itemsService;

    private final ConnectedServersService connectedServersService;


    public BlizzardApiRequests(ItemsService itemsService, ConnectedServersService connectedServersService) {
        this.itemsService = itemsService;
        this.connectedServersService = connectedServersService;
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


    public List<ConnectedServersModel> getListOfConnectedServers() {

        ObjectMapper mapper = new ObjectMapper();
        String token = BlizzardJWTToken.getToken();
        List<ConnectedServersModel> listOfConnectedSeverModels = new ArrayList<>();

        //Skip first value because it is 'All'
        for (int i = 1; i < Regions.values().length; i++) {
            listOfConnectedSeverModels.addAll(getRegionConnectedRealms(token, Regions.values()[i], mapper));
        }

        List<Server> servers = getListOfServers();

        for (ConnectedServersModel connectedServerModel : listOfConnectedSeverModels) {
            configureConnectedServer(token, connectedServerModel, mapper, servers);
        }

        return listOfConnectedSeverModels;
    }

    private void configureConnectedServer(String token,
                                          ConnectedServersModel connectedServerModel,
                                          ObjectMapper mapper,
                                          List<Server> servers) {

        String request = generateRequestForConnectedServers(token, connectedServerModel);
        String json = executeApiRequest(request);
        List<String> serverNames = new ArrayList<>();

        try {

            JsonNode parent = mapper.readTree(json);
            JsonNode realms = parent.get("realms");

            for (JsonNode realm : realms) {
                serverNames.add(realm.get("slug").textValue());
            }

        } catch (JsonProcessingException e) {
            //TODO exception handling
            e.printStackTrace();
        }

        for (String serverName : serverNames) {

            Server toSearchFor = new Server();
            toSearchFor.setSlug(serverName);
            toSearchFor.setRegion(connectedServerModel.getRegion().getValue());

            int index = servers.indexOf(toSearchFor);
            Server server1 = servers.get(index);
            server1.setConnectedServersModel(connectedServerModel);
            connectedServerModel.getServers().add(server1);

        }

    }

    private List<ConnectedServersModel> getRegionConnectedRealms(String token, Regions region, ObjectMapper mapper) {

        String request = generateConnectedServersListRequest(token, region.getValue());

        String connectedRealmJasonData = executeApiRequest(request);

        JsonNode parent;

        try {

            parent = mapper.readTree(connectedRealmJasonData);
            JsonNode realmsJson = parent.path("connected_realms");
            ConnectedServersModel[] convertedLines = mapper.convertValue(realmsJson, ConnectedServersModel[].class);

            for (ConnectedServersModel convertedLine : convertedLines) {
                convertedLine.setRegion(region);
                convertedLine.setId();
            }

            return Arrays.asList(convertedLines);

        } catch (JsonProcessingException e) {
            //TODO exception handling
            e.printStackTrace();
        }

        return new ArrayList<>();

    }


    public List<AuctionEntity> getAuctionHouse() {

        ObjectMapper auctionMapper = new ObjectMapper();
        List<AuctionEntity> listOfParsedAuctionEntities = new ArrayList<>();
        List<ConnectedServersModel> connectedServersModels = connectedServersService.findAll();
        String token = BlizzardJWTToken.getToken();
        List<ItemEntity> itemsToSearchFor = itemsService.findAllItemEntities();

        SimpleModule module = new SimpleModule("AuctionHouseResponseDeserializer",
                new Version(1, 0, 0, null, null, null));
        module.addDeserializer(AuctionEntity.class, new AuctionHouseResponseDeserializer());

        auctionMapper.registerModule(module);
        auctionMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        for (ConnectedServersModel connectedServersModel : connectedServersModels) {

            List<AuctionEntity> auctionHouseEntities = getConnectedServerAuctionHouse(token, connectedServersModel, auctionMapper);
            System.out.println("Working on server: " + connectedServersModel.getConnectedServerId() +
                    "  Items :" + auctionHouseEntities.size());

            for (AuctionEntity auctionHouseEntity : auctionHouseEntities) {
                for (ItemEntity itemEntity : itemsToSearchFor) {
                    if (auctionHouseEntity.getItemId().equals(itemEntity.getId())) {
                        auctionHouseEntity.setConnectedServerId(connectedServersModel.getConnectedServerId());
                        listOfParsedAuctionEntities.add(auctionHouseEntity);
                        break;
                    }
                }
            }
        }
        return listOfParsedAuctionEntities;

    }

    public List<AuctionEntity> getConnectedServerAuctionHouse(String token,
                                                              ConnectedServersModel connectedServersModel,
                                                              ObjectMapper mapper) {
        String request = generateAuctionHouseRequest(BlizzardJWTToken.getToken(), connectedServersModel);
        String jsonData = executeApiRequest(request);

        try {
            JsonNode jsonWithAllInformation = mapper.readTree(jsonData);

            JsonNode jsonWithAuctions = jsonWithAllInformation.path("auctions");
            return mapper.convertValue(jsonWithAuctions, new TypeReference<>() {
            });

        } catch (JsonProcessingException e) {
            //TODO exception handling
            e.printStackTrace();

        }

        return new ArrayList<>();
    }


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

    private String generateAuctionHouseRequest(String token, ConnectedServersModel connectedServersModel) {
        return "curl -X GET " +
                "https://" +
                connectedServersModel.getRegion().getValue() +
                ".api.blizzard.com/data/wow/connected-realm/" +
                connectedServersModel.getConnectedServerId() +
                "/auctions?" +
                "namespace=dynamic-" +
                connectedServersModel.getRegion().getValue() +
                "&locale=en_US&access_token=" +
                token;
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

    public String generateConnectedServersListRequest(String token, String region) {
        return "curl " +
                "-X GET https://" +
                region.toLowerCase() +
                ".api.blizzard.com/data/wow/connected-realm/index?namespace=dynamic-" +
                region.toLowerCase() +
                "&locale=en_US&access_token=" +
                token;
    }

    public String generateRequestForConnectedServers(String token, ConnectedServersModel connectedServerModel) {
        return "curl " +
                "-X GET " +
                "https://" +
                connectedServerModel.getRegion().getValue().toLowerCase() +
                ".api.blizzard.com/data/wow/connected-realm/" +
                connectedServerModel.extractId() +
                "?namespace=dynamic-" +
                connectedServerModel.getRegion().getValue().toLowerCase() +
                "&locale=en_US&access_token=" +
                token;
    }
}