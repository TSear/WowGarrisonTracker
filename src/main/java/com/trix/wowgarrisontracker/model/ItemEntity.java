package com.trix.wowgarrisontracker.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Table(name = "itemEntity")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ItemEntity {

    @Id
    private Long id;
    private String name;

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "itemEntity")
    private List<AuctionEntity> auctions;


    public ItemEntity(Long id) {
        this.id = id;
    }

    public ItemEntity(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ItemEntity) {
            return this.id.equals(((ItemEntity) obj).getId());
        }
        return this.id == obj;
    }


}
