package com.trix.wowgarrisontracker.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "itemEntity")
@Getter
@Setter
@NoArgsConstructor
@Entity
public class ItemEntity {

    @Id
    private Long id;
    private String name;


    public ItemEntity(Long id) {
        this.id = id;
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
