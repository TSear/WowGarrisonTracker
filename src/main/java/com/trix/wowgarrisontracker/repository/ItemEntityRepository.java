package com.trix.wowgarrisontracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.trix.wowgarrisontracker.model.ItemEntity;

public interface ItemEntityRepository extends JpaRepository<ItemEntity, Long> {

}
