package com.trix.wowgarrisontracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.trix.wowgarrisontracker.model.ItemEntity;

@Repository
public interface ItemEntityRepository extends JpaRepository<ItemEntity, Long> {


	
}
