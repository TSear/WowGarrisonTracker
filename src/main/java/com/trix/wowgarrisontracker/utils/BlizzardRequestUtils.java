package com.trix.wowgarrisontracker.utils;

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
import java.time.LocalTime;
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
    private static LocalTime EXPIRATION_TIME;

    private final Logger logger = LogManager.getLogger("blizzard.request.utils");

    @PostConstruct
    private void init() {
        if (!isTokenValid())
            getAccessToken();
    }

    public Auctions getAuctionHouse() {
        ObjectMapper auctionMapper = new ObjectMapper();
        String jsonData = "";
        ObjectMapper mapper = new ObjectMapper();
        Auctions ah = new Auctions();
        try {
            jsonData = getJsonData("curl -X GET https://eu.api.blizzard.com/data/wow/connected-realm/3713/auctions?"
                    + "namespace=dynamic-eu&locale=en_US&access_token=" + TOKEN);
            SimpleModule module = new SimpleModule("AuctionHouseResponseDeserializer",
                    new Version(1, 0, 0, null, null, null));
            module.addDeserializer(AuctionEntity.class, new AuctionHouseResponseDeserializer());
            auctionMapper.registerModule(module);
            auctionMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
            ah = auctionMapper.readValue(jsonData, Auctions.class);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return ah;

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

    public void getAccessToken() {
        String token = "";

        String jsonData = this.getJsonData(
                "curl -u " + client_id + ":" + client_secret + " -d grant_type=client_credentials https://us.battle.net/oauth/token");

        ObjectMapper mapper = new ObjectMapper();

        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try {
            Map<String, Object> mappedString = mapper.readValue(jsonData, new TypeReference<Map<String, Object>>() {
            });
            token = mappedString.get("token_type") + " " + mappedString.get("access_token");
        } catch (JsonProcessingException e) {
            logger.error("Error occurred during token request");
            e.printStackTrace();
        }

        BlizzardRequestUtils.TOKEN = token.substring(7);
        BlizzardRequestUtils.EXPIRATION_TIME = LocalTime.now();
    }

    public List<Server> getListOfServers() {

        refreshTokenIfInvalid();

        ObjectMapper mapper = new ObjectMapper();
        String json = "";
        List<String> regions = Arrays.stream(Regions.values())
                .map(Regions::getValue)
                .collect(Collectors.toList());
        List<Server> servers = new ArrayList<>();

        for (String region : regions) {
            json = getJsonData("curl -X GET https://" + region.toUpperCase() + ".api.blizzard.com/data/wow/realm/index?namespace=dynamic-" + region + "&locale=en_US&access_token=" + TOKEN);
            JsonNode parent = null;

            try {
                parent = mapper.readTree(json);
            } catch (JsonProcessingException e) {
                logger.error("Exception occurred during parsing list of servers");
            }

            JsonNode realms = parent.path("realms");
            servers.addAll(Arrays.stream(mapper.convertValue(realms, Server[].class)).
                    peek(server -> server.setRegion(region))
                    .collect(Collectors.toList()));
        }


        return servers;
    }

    public void refreshTokenIfInvalid() {
        if (!isTokenValid())
            getAccessToken();
    }

    public static boolean isTokenValid() {
        if (EXPIRATION_TIME == null || TOKEN == null)
            return false;

        if (EXPIRATION_TIME.isAfter(EXPIRATION_TIME.plusHours(23)))
            return false;

        return true;
    }

}
