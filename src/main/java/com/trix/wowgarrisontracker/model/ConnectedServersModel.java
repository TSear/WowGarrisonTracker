package com.trix.wowgarrisontracker.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.trix.wowgarrisontracker.enums.Regions;
import lombok.Getter;
import lombok.Setter;
import org.atmosphere.config.service.Get;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity(name = "connectedServers")
public class ConnectedServersModel {

    @Id
    private int connectedServerId;

    @Transient
    @JsonProperty("href")
    private String href;

    @OneToMany(mappedBy = "connectedServersModel", cascade = CascadeType.ALL)
    private List<Server> servers;

    @Transient
    private Regions region;

    public ConnectedServersModel() {
        servers = new ArrayList<>();
    }

    public int extractId(){
        int endingIndex = href.indexOf("?");
        int startingIndex = href.lastIndexOf("/",endingIndex);
        return Integer.parseInt(href.substring(startingIndex+1,endingIndex));
    }

    public void setId(){
        this.connectedServerId = extractId();
    }
}
