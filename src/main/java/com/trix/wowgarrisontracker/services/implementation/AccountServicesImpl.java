package com.trix.wowgarrisontracker.services.implementation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.trix.wowgarrisontracker.converters.AccountToAccountPojo;
import com.trix.wowgarrisontracker.converters.EntryToEntryPojo;
import com.trix.wowgarrisontracker.model.Account;
import com.trix.wowgarrisontracker.model.AccountCharacter;
import com.trix.wowgarrisontracker.model.Entry;
import com.trix.wowgarrisontracker.model.LoginRequest;
import com.trix.wowgarrisontracker.pojos.AccountPojo;
import com.trix.wowgarrisontracker.pojos.EntryPojo;
import com.trix.wowgarrisontracker.pojos.RegisterModel;
import com.trix.wowgarrisontracker.repository.AccountRepository;
import com.trix.wowgarrisontracker.services.interfaces.AccountCharacterService;
import com.trix.wowgarrisontracker.services.interfaces.AccountService;
import com.trix.wowgarrisontracker.services.interfaces.EntryService;

import lombok.extern.slf4j.Slf4j;

@Service
public class AccountServicesImpl implements AccountService {

	private AccountRepository accountRepository;
	private Logger logger = LoggerFactory.getLogger(Slf4j.class);
	private AccountToAccountPojo accountToAccountPojo;
	private AccountCharacterService accountCharacterService;
	private EntryService entryService;
	private EntryToEntryPojo entryToEntryPojo;
	private PasswordEncoder passwordEncoder;

	public AccountServicesImpl(AccountRepository accountRepository, AccountToAccountPojo accountToAccountPojo,
			EntryService entryService, AccountCharacterService accountCharacterService,
			EntryToEntryPojo entryToEntryPojo, PasswordEncoder passwordEncoder) {
		this.accountRepository = accountRepository;
		this.accountToAccountPojo = accountToAccountPojo;
		this.accountCharacterService = accountCharacterService;
		this.entryService = entryService;
		this.entryToEntryPojo = entryToEntryPojo;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public void save(Account account) {

		if (!account.getLogin()
				.isEmpty() && this.findUserByUsername(account.getLogin()) == null) {
			account.setPassword(passwordEncoder.encode(account.getPassword()));
			accountRepository.save(account);
		}

	}

	@Override
	public void delete(Long id) {

		if (accountRepository.existsById(id))
			accountRepository.deleteById(id);
		else
			logger.info("Nie znaleziono konta");

	}

	@Override
	public void update(Account account, Long id) {

		if (accountRepository.existsById(id)) {
			account.setId(id);
			accountRepository.save(account);
		} else {
			logger.info("Nie znaleziono konta");
		}

	}

	@Override
	public List<AccountPojo> findAll() {
		List<AccountPojo> accountPojos = new ArrayList<>();

		for (Account tmp : accountRepository.findAll()) {
			accountPojos.add(accountToAccountPojo.convert(tmp));
		}
		return accountPojos;
	}

	@Override
	public Account findUserByUsername(String username) {
		Optional<Account> optionalAccount = accountRepository.findByLogin(username);
		return optionalAccount.isPresent() ? optionalAccount.get() : null;

	}

	@Override
	public boolean correctCredentials(Account inDatabase, LoginRequest fromForm) {

		return inDatabase.getLogin()
				.equals(fromForm.getLogin())
				&& inDatabase.getPassword()
						.equals(fromForm.getPassword());

	}

	@Override
	public Account correctCredentials(LoginRequest fromForm) {

		Account account = this.findUserByUsername(fromForm.getLogin());
		if (account != null && account.getPassword()
				.equals(fromForm.getPassword())) {
			return account;
		}
		return null;
	}

	@Override
	public boolean isExisting(LoginRequest loginRequest) {
		Account account = this.findUserByUsername(loginRequest.getLogin());
		if (account == null)
			return false;
		return passwordEncoder.matches(loginRequest.getPassword(),account.getPassword());
		}

	@Override
	public List<EntryPojo> getAllEntries(Long accountId) {

		List<AccountCharacter> listOfAccountCharacters = accountCharacterService.listOfAccountCharacters(accountId);
		List<EntryPojo> listOfAllEntries = new ArrayList<>();
		for (AccountCharacter tmp : listOfAccountCharacters) {
			listOfAllEntries.addAll(entryService.listOfEntries(tmp.getId())
					.stream()
					.map(entryToEntryPojo::convert)
					.collect(Collectors.toList()));
		}

		return listOfAllEntries;
	}

	@Override
	public boolean areCredentialsTaken(RegisterModel registerModel) {
		Account account = this.findUserByUsername(registerModel.getLogin());
		if (account != null)
			return true;
		return false;
	}

	@Override
	public Map<String, Long> getAllResourcesByAccountId(Long id) {

		Map<String, Long> resources = new HashMap<>();
		Long warPaint = 0l;
		Long garrisonResources = 0l;
		List<Entry> listOfEntries = new ArrayList<>();
		accountCharacterService.listOfAccountCharacters(id)
				.stream()
				.forEach(character -> listOfEntries.addAll(entryService.listOfEntries(character.getId())));

		garrisonResources = listOfEntries.stream()
				.collect(Collectors.summingLong(Entry::getGarrisonResources));
		warPaint = listOfEntries.stream()
				.collect(Collectors.summingLong(Entry::getWarPaint));

		resources.put("garrisonResources", garrisonResources);
		resources.put("warPaint", warPaint);

		return resources;

	}

	@Override
	public Account findById(Long id) {
		Optional<Account> optionalAccount = accountRepository.findById(id);
		return optionalAccount.isPresent() ? optionalAccount.get() : null;
	}

	@Override
	public boolean saveEntry(EntryPojo entryPojo) {
		AccountCharacter accountCharacter = accountCharacterService.findById(entryPojo.getAccountCharacterId());
		Account account = this.findById(accountCharacter.getAccount().getId());
		account.setGarrisonResources(account.getGarrisonResources() + entryPojo.getGarrisonResources());
		account.setWarPaint(account.getWarPaint() + entryPojo.getWarPaint());
		account.setAmountOfEntries(account.getAmountOfEntries() + 1);
		this.update(account, account.getId());
		entryService.save(entryPojo);
		return true;

	}

	


}

