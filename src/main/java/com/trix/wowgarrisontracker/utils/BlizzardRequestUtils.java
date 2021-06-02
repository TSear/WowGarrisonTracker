package com.trix.wowgarrisontracker.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.trix.wowgarrisontracker.deserializers.AuctionHouseResponseDeserializer;
import com.trix.wowgarrisontracker.enums.Regions;
import com.trix.wowgarrisontracker.model.AuctionEntity;
import com.trix.wowgarrisontracker.model.Auctions;
import com.trix.wowgarrisontracker.model.Server;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class BlizzardRequestUtils {

    @Value("${client.id}")
    private String client_id;

    @Value("${client.secret}")
    private String client_secret;

    private static String TOKEN;
    private static LocalDateTime EXPIRATION_TIME;

    private final Logger logger = LogManager.getLogger("blizzard.request.utils");

    @PostConstruct
    private void init() {
        if (!isTokenValid(getExpirationTime())) {
            try {
                getAccessToken();
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }

    public List<AuctionEntity> getAuctionHouse() {

        ObjectMapper auctionMapper = new ObjectMapper();
        List<AuctionEntity> listOfParsedAuctionEntities = new ArrayList<>();

        //This module is needed to parse nested data
        //TODO read more about json parsing
        SimpleModule module = new SimpleModule("AuctionHouseResponseDeserializer",
                new Version(1, 0, 0, null, null, null));
        module.addDeserializer(AuctionEntity.class, new AuctionHouseResponseDeserializer());

        auctionMapper.registerModule(module);
        auctionMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        String stringJsonData = getJsonData("curl -X GET https://eu.api.blizzard.com/data/wow/connected-realm/3713/auctions?"
                + "namespace=dynamic-eu&locale=en_US&access_token=" + getTokenKey());

        try {
            JsonNode jsonWithAllInformations = auctionMapper.readTree(stringJsonData);

            JsonNode jsonWithAuctions = jsonWithAllInformations.path("auctions");
            listOfParsedAuctionEntities = auctionMapper.convertValue(jsonWithAuctions, new TypeReference<List<AuctionEntity>>() {
            });

        } catch (JsonProcessingException e) {
            logger.error("Error occurred while parsing auction house json");
            e.printStackTrace();
        }

        return listOfParsedAuctionEntities;

    }

    public String getJsonData(String curlRequest) {
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

    public void getAccessToken() throws JsonProcessingException {
        String token = "";

        String jsonData = this.getJsonData(
                "curl -u " + client_id + ":" + client_secret + " -d grant_type=client_credentials https://us.battle.net/oauth/token");

        ObjectMapper mapper = new ObjectMapper();

        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        Map<String, Object> mappedString = mapper.readValue(jsonData, new TypeReference<Map<String, Object>>() {
        });
        if (mappedString.get("token_type") != null && mappedString.get("access_token") != null)
            token = mappedString.get("token_type") + " " + mappedString.get("access_token");

        BlizzardRequestUtils.TOKEN = token;
        BlizzardRequestUtils.EXPIRATION_TIME = LocalDateTime.now().plusHours(23);
    }

    public List<Server> getListOfServers() {

        refreshTokenIfInvalid();

        ObjectMapper mapper = new ObjectMapper();
        String json = "";
        List<String> regions = Arrays.stream(Regions.values())
                .skip(1)
                .map(Regions::getValue)
                .collect(Collectors.toList());
        List<Server> servers = new ArrayList<>();

        for (String region : regions) {
            json = getJsonData("curl -X GET https://" + region.toUpperCase() + ".api.blizzard.com/data/wow/realm/index?namespace=dynamic-" + region + "&locale=en_US&access_token=" + getTokenKey());
            JsonNode parent = null;

            try {
                parent = mapper.readTree(json);
            } catch (JsonProcessingException e) {
                logger.error("Exception occurred during parsing list of servers");
            }

            JsonNode realms = parent.path("realms");
            Server[] serverValues = mapper.convertValue(realms, Server[].class);
            Arrays.stream(serverValues).forEach(server -> server.setRegion(region));
            servers = Arrays.asList(serverValues);

        }


        return servers;
    }

    public void refreshTokenIfInvalid() {
        if (!isTokenValid(BlizzardRequestUtils.EXPIRATION_TIME)) {
            try {
                getAccessToken();
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getTokenKey() {
        return BlizzardRequestUtils.TOKEN.substring(7);
    }

    public static boolean isTokenValid(LocalDateTime expiration) {

        if (TOKEN == null || !TOKEN.startsWith("bearer"))
            return false;

        if (expiration == null)
            return false;

        return LocalDateTime.now().isBefore(expiration);
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public void setClient_secret(String client_secret) {
        this.client_secret = client_secret;
    }

    public String getToken() {
        return TOKEN;
    }

    public static LocalDateTime getExpirationTime() {
        return EXPIRATION_TIME;
    }
}
