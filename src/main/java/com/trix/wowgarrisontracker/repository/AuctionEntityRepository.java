package com.trix.wowgarrisontracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.trix.wowgarrisontracker.model.AuctionEntity;

public interface AuctionEntityRepository extends JpaRepository<AuctionEntity, Long>{

}
