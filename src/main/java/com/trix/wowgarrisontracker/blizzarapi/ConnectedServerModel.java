package com.trix.wowgarrisontracker.blizzarapi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConnectedServerModel {

    @JsonProperty("href")
    private String value;

    public int extractId(){
        int endingIndex = value.indexOf("?");
        int startingIndex = value.lastIndexOf("/",endingIndex);
        int connectedServerId = Integer.parseInt(value.substring(startingIndex+1,endingIndex));
        return connectedServerId;
    }
}
