package com.trix.wowgarrisontracker.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

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
}
