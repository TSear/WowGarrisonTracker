package com.trix.wowgarrisontracker.services.implementation;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.trix.wowgarrisontracker.model.AuctionEntity;
import com.trix.wowgarrisontracker.model.ItemEntity;
import com.trix.wowgarrisontracker.repository.AuctionEntityRepository;
import com.trix.wowgarrisontracker.repository.ItemEntityRepository;
import com.trix.wowgarrisontracker.services.interfaces.AuctionService;

@Service
public class AuctionServiceImpl implements AuctionService{

	private ItemEntityRepository itemRepository;
	private AuctionEntityRepository repository;
	

	public AuctionServiceImpl(AuctionEntityRepository repository, ItemEntityRepository itemRepository) {
		this.repository = repository;
		this.itemRepository = itemRepository;
	}
	
	

	@Override
	public boolean save(AuctionEntity auctionEntity) {
		return repository.save(auctionEntity)!=null;
	}


	@Override
	public List<AuctionEntity> getAuctionsByItemId(Long itemId) {
		
		return repository.findAuctionEntityByItemId(itemId);
	}


	@Override
	public String getItemNameByAuction(Long auctionItemId) {
		
		Optional<ItemEntity> optionalItem = itemRepository.findById(auctionItemId);
		if(optionalItem.isPresent()) {
			return optionalItem.get().getName();
		}
		
		return "";
	}

	@Override
	public void removeAll() {
		repository.deleteAll();
	}

	
	
	
	
	
}
