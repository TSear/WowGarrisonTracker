package com.trix.wowgarrisontracker.blizzarapi;

import com.fasterxml.jackson.core.JsonProcessingException;
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

import static com.trix.wowgarrisontracker.blizzarapi.BlizzardJWTToken.getToken;

@Component
public class BlizzardApiRequests {


    private final ItemsService itemsService;

    private final ConnectedServersService connectedServersService;

    private final ObjectMapper auctionsMapper;


    public BlizzardApiRequests(ItemsService itemsService, ConnectedServersService connectedServersService) {
        this.itemsService = itemsService;
        this.connectedServersService = connectedServersService;
        auctionsMapper = new ObjectMapper();

        SimpleModule module = new SimpleModule("AuctionHouseResponseDeserializer");
        module.addDeserializer(AuctionEntity.class, new AuctionHouseResponseDeserializer());

        auctionsMapper.registerModule(module);
        auctionsMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

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
        String token = getToken();
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

    public List<AuctionEntity> getFilteredAuctions() {
        return getFilteredAuctions(connectedServersService.findAll());
    }

    public List<AuctionEntity> getFilteredAuctions(List<ConnectedServersModel> connectedServersModels) {

        List<AuctionEntity> listOfParsedAuctionEntities = new ArrayList<>();
        List<ItemEntity> itemsToSearchFor = itemsService.findAll();

        for (ConnectedServersModel connectedServersModel : connectedServersModels) {

            List<AuctionEntity> auctionHouseEntities = getAuctions(connectedServersModel);

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

    public List<AuctionEntity> getAuctions(ConnectedServersModel connectedServersModel) {

        String request = generateAuctionsRequest(getToken(), connectedServersModel);
        String jsonData = executeApiRequest(request);
        List<AuctionEntity> auctionEntities = new ArrayList<>();

        try {
            auctionEntities = parseAuctionsJson(jsonData);
        } catch (JsonProcessingException e) {
            //TODO exception handling
            e.printStackTrace();
        }

        return auctionEntities;
    }


    public List<AuctionEntity> parseAuctionsJson(String jsonData) throws JsonProcessingException {

        JsonNode rootJsonNode = auctionsMapper.readTree(jsonData);
        JsonNode auctionsJsonNode = rootJsonNode.path("auctions");
        return auctionsMapper.convertValue(auctionsJsonNode, new TypeReference<>() {
        });

    }


    public List<Server> getListOfServers() {

        String token = getToken();

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

    private String generateAuctionsRequest(String token, ConnectedServersModel connectedServersModel) {
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