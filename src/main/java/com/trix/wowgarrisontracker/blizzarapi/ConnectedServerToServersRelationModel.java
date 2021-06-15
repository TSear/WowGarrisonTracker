package com.trix.wowgarrisontracker.blizzarapi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ConnectedServerToServersRelationModel {

    @JsonProperty("id")
    private int connectedServerId;

    private List<String> serverNames;

    @SuppressWarnings("unchecked")
    @JsonProperty("realms")
    private void setServerNames(Map<String, Object> array){
        serverNames.add((String)array.get("slug"));
    }
}
