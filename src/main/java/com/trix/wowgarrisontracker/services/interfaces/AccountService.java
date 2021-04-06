package com.trix.wowgarrisontracker.services.interfaces;

import java.util.List;
import java.util.Map;

import com.trix.wowgarrisontracker.exceptions.AccountNotFoundException;
import com.trix.wowgarrisontracker.model.Account;
import com.trix.wowgarrisontracker.model.LoginRequest;
import com.trix.wowgarrisontracker.pojos.AccountPojo;
import com.trix.wowgarrisontracker.pojos.EntryPojo;
import com.trix.wowgarrisontracker.pojos.RegisterModel;
import com.trix.wowgarrisontracker.pojos.RegisterPojo;

public interface AccountService {

	void saveAccount(Account account);

	void createAccount(RegisterPojo registerPojo);

	void deleteAccount(Long id);

	List<AccountPojo> findAllAccounts();

	Account findUserByUsername(String username);

	Account correctCredentials(LoginRequest fromForm);

	Account areCredentialsCorrect(String login, String password) throws AccountNotFoundException;

	boolean isAccountInDatabase(LoginRequest loginRequest);


	boolean areCredentialsTaken(RegisterModel registerModel);

	Account findById(Long id);



}
