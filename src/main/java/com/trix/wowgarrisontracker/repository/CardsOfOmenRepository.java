package com.trix.wowgarrisontracker.repository;

import com.trix.wowgarrisontracker.model.CardsOfOmen;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardsOfOmenRepository extends JpaRepository<CardsOfOmen, Long> {

    List<CardsOfOmen> findAllByAccountId(Long accountId);

    List<CardsOfOmen> findAllByAccountIdOrderByLocalDateDesc(Long accountId, Pageable pageable);


    int countByAccountId(Long accountId);
}
