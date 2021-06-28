package com.trix.wowgarrisontracker.services.interfaces;

import com.trix.wowgarrisontracker.model.CardsOfOmen;

import java.util.List;

public interface CardsOfOmenService {

   List<CardsOfOmen> findAll();

   List<CardsOfOmen> findAllByAccountId(Long accountId);

   boolean save(CardsOfOmen cards);

   boolean delete(CardsOfOmen cards);

   List<CardsOfOmen> findAllByAccountIdPaged(Long accountId, int offset, int limit);

   int getAmountOfEntries(Long accountId);

   Long sumTotalOpenedCards(Long accountId);

   Long sumTotalGold(Long accountId);
}
