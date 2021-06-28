package com.trix.wowgarrisontracker.repository;

import com.trix.wowgarrisontracker.model.CardsOfOmen;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardsOfOmenRepository extends JpaRepository<CardsOfOmen, Long> {

    List<CardsOfOmen> findAllByAccountId(Long accountId);

    List<CardsOfOmen> findAllByAccountIdOrderByLocalDateDesc(Long accountId, Pageable pageable);

    int countByAccountId(Long accountId);

    @Query("SELECT sum(card.moneyFromCards) FROM CardsOfOmen card")
    Long sumMoneyFromCardsByAccountId(Long accountId);

    @Query("SELECT sum(card.amountOfCards) FROM CardsOfOmen card")
    Long sumAmountOfCardsByAccountId(Long accountId);
}
