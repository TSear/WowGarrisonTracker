package com.trix.wowgarrisontracker.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.trix.wowgarrisontracker.model.AuctionEntity;

public interface AuctionEntityRepository extends JpaRepository<AuctionEntity, Long>{
	
	List<AuctionEntity> findAuctionEntityByItemId(Long itemId);

}
