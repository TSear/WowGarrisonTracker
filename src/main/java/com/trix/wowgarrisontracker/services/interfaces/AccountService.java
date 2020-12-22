package com.trix.wowgarrisontracker.services.interfaces;

import java.util.List;

import com.trix.wowgarrisontracker.model.Account;

public interface AccountService {

	void save(Account account);

	void delete(Long id);

	void update(Account account, Long id);

	List<Account> findAll();
    
}
