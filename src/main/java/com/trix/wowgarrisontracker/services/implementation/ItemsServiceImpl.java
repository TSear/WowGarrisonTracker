package com.trix.wowgarrisontracker.services.implementation;

import com.trix.wowgarrisontracker.model.ItemEntity;
import com.trix.wowgarrisontracker.repository.ItemEntityRepository;
import com.trix.wowgarrisontracker.services.interfaces.ItemsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemsServiceImpl implements ItemsService {

    private final ItemEntityRepository repository;

    public ItemsServiceImpl(ItemEntityRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean save(ItemEntity itemEntity) {
        if (itemEntity != null) {
            repository.save(itemEntity);
            return true;
        }
        return false;
    }

    @Override
    public List<ItemEntity> findAll() {
        return repository.findAll();
    }

    @Override
    public int count() {
        return (int) repository.count();
    }

    @Override
    public void saveAll(List<ItemEntity> itemEntities) {
        repository.saveAll(itemEntities);
    }


}
