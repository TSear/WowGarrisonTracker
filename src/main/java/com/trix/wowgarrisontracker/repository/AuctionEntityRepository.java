package com.trix.wowgarrisontracker.repository;

import com.trix.wowgarrisontracker.model.AuctionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuctionEntityRepository extends JpaRepository<AuctionEntity, Long> {

    List<AuctionEntity> findAuctionEntityByItemId(Long itemId);

    List<AuctionEntity> findByConnectedServerId(Integer connectedServerId);

}
