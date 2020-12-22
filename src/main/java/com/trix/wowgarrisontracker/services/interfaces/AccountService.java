package com.trix.wowgarrisontracker.services.interfaces;

import java.util.List;

import com.trix.wowgarrisontracker.model.Account;
import com.trix.wowgarrisontracker.pojos.AccountPojo;

public interface AccountService {

	void save(Account account);

	void delete(Long id);

	void update(Account account, Long id);

	List<AccountPojo> findAll();
    
}
