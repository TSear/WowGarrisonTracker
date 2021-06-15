package com.trix.wowgarrisontracker.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "server")
public class Server {

    @JsonProperty("name")
    private String name;

    @JsonProperty("id")
    @Id
    private Integer id;

    @JsonProperty("slug")
    private String slug;

    private String region;

    @OneToMany(mappedBy = "server")
    private List<Options> options;

    @ManyToOne
    @JoinColumn(referencedColumnName = "connectedServerId", name = "connectedServerId")
    private ConnectedServersModel connectedServersModel;

    public Server() {
        this.name = "";
        this.region = "";
    }

    public Server(Integer id) {
        this();
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Server server = (Server) o;
        return slug.equals(server.slug) && region.equals(server.region);
    }

    @Override
    public int hashCode() {
        return Objects.hash(slug, region);
    }

    @Override
    public String toString() {
        return name + " - " + region.toUpperCase();
    }
}
