package com.trix.wowgarrisontracker.services.implementation;

import com.trix.wowgarrisontracker.model.ItemEntity;
import com.trix.wowgarrisontracker.repository.ItemEntityRepository;
import com.trix.wowgarrisontracker.services.interfaces.ItemEntityService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemEntityServiceImpl implements ItemEntityService {

    private final ItemEntityRepository repository;

    public ItemEntityServiceImpl(ItemEntityRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean save(ItemEntity itemEntity) {
        if(itemEntity!=null){
            repository.save(itemEntity);
            return true;
        }
        return false;
    }

    @Override
    public List<ItemEntity> findAllItemEntities() {
        return repository.findAll();
    }

}
