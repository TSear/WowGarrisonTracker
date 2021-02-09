package com.trix.wowgarrisontracker.services.implementation;

import org.springframework.stereotype.Service;

import com.trix.wowgarrisontracker.model.ItemEntity;
import com.trix.wowgarrisontracker.repository.ItemEntityRepository;
import com.trix.wowgarrisontracker.services.interfaces.ItemEntityService;

@Service
public class ItemEntityServiceImpl implements ItemEntityService {

	private ItemEntityRepository repository;

	public ItemEntityServiceImpl(ItemEntityRepository repository) {
		this.repository = repository;
	}

	@Override
	public boolean save(ItemEntity itemEntity) {
		return repository.save(itemEntity)!=null;
	}

}
