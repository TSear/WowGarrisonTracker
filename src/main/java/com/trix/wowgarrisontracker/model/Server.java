package com.trix.wowgarrisontracker.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.swing.text.html.Option;
import java.util.List;

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

    public Server(){
        this.name = "";
        this.region = "";
    }

    public Server(Integer id){
        this();
        this.id=id;
    }



    @Override
    public String toString() {
        return name + " - " + region.toUpperCase();
    }
}
