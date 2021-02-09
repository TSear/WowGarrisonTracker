package com.trix.wowgarrisontracker.services.implementation;

import org.springframework.stereotype.Service;

import com.trix.wowgarrisontracker.model.AuctionEntity;
import com.trix.wowgarrisontracker.repository.AuctionEntityRepository;
import com.trix.wowgarrisontracker.services.interfaces.AuctionService;

@Service
public class AuctionServiceImpl implements AuctionService{

	private AuctionEntityRepository repository;
	private static long row = 0;
	

	public AuctionServiceImpl(AuctionEntityRepository repository) {
		this.repository = repository;
	}


	@Override
	public boolean save(AuctionEntity auctionEntity) {
		System.out.println(row + ": Zapisywanie");
		row++;
		return repository.save(auctionEntity)!=null;
	}

	
	
}
