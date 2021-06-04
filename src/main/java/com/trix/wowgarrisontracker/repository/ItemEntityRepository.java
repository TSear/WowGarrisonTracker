package com.trix.wowgarrisontracker.repository;

import com.trix.wowgarrisontracker.model.ItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemEntityRepository extends JpaRepository<ItemEntity, Long> {


}
