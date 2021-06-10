package com.trix.wowgarrisontracker.blizzarapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class BlizzardJWTToken {

    private static final String ACCESS_TOKEN = "access_token";

    private static final String TOKEN_TYPE = "Bearer_";

    private static String token = null;

    private static LocalDateTime expirationTime;

    @Value("${client.id}")
    private static String clientId;

    @Value("${client.secret}")
    private static String clientSecret;


    private BlizzardJWTToken() {
    }

    @Autowired
    private void setUp(@Value("${client.id}") String clientId, @Value("${client.secret}") String clientSecret) {
        BlizzardJWTToken.clientSecret = clientSecret;
        BlizzardJWTToken.clientId = clientId;
    }

    static public String getToken() {

        if (token == null || LocalDateTime.now().isAfter(expirationTime)) {
            requestToken();
        }
        return token;

    }

    public static String tokenType() {
        return BlizzardJWTToken.TOKEN_TYPE;
    }

    private static void requestToken() {

        String curlRequest = generateCurlRequest();
        String jsonData = collectJsonData(curlRequest);
        parseJsonTokenData(jsonData);

    }

    private static String collectJsonData(String curlRequest) {

        String jsonData = "";

        try {
            Process process = Runtime.getRuntime().exec(curlRequest);
            InputStream inputStream = process.getInputStream();
            jsonData = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))
                    .lines()
                    .collect(Collectors.joining("\n"));
        } catch (IOException e) {
            //TODO addWS exception handling
            e.printStackTrace();
        }
        return jsonData;

    }

    private static String generateCurlRequest() {

        return "curl " +
                "-u " + BlizzardJWTToken.clientId + ":" + BlizzardJWTToken.clientSecret + " " +
                "-d grant_type=client_credentials " +
                "https://us.battle.net/oauth/token";

    }

    private static void parseJsonTokenData(String curlRequest) {

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try {
            Map<String, String> mappedValues = mapper.readValue(curlRequest, new TypeReference<>() {
            });
            BlizzardJWTToken.token = mappedValues.get(ACCESS_TOKEN);
            BlizzardJWTToken.expirationTime = LocalDateTime.now().plusHours(23);
        } catch (JsonProcessingException e) {
            //TODO Exception handling
            e.printStackTrace();
        }
    }

}
