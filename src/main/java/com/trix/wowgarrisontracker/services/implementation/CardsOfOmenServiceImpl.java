package com.trix.wowgarrisontracker.services.implementation;

import com.trix.wowgarrisontracker.model.CardsOfOmen;
import com.trix.wowgarrisontracker.repository.CardsOfOmenRepository;
import com.trix.wowgarrisontracker.services.interfaces.CardsOfOmenService;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardsOfOmenServiceImpl implements CardsOfOmenService {

    private final CardsOfOmenRepository repository;

    public CardsOfOmenServiceImpl(CardsOfOmenRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<CardsOfOmen> findAll() {
        return repository.findAll();
    }

    @Override
    public List<CardsOfOmen> findAllByAccountId(Long accountId) {
        return repository.findAllByAccountId(accountId);
    }

    @Override
    public boolean save(CardsOfOmen cards) {

        if (cards == null)
            return false;

        repository.save(cards);
        return true;
    }

    @Override
    public boolean delete(CardsOfOmen cards) {
        if (cards == null)
            return false;
        repository.delete(cards);
        return true;
    }

    @Override
    public List<CardsOfOmen> findAllByAccountIdPaged(Long accountId, int offset, int limit) {
        return repository.findAllByAccountId(accountId, PageRequest.of(offset, limit));
    }

    @Override
    public int getAmountOfEntries(Long accountId) {
        return repository.countByAccountId(accountId);
    }
}
