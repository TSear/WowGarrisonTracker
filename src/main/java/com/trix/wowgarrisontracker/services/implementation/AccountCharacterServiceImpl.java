package com.trix.wowgarrisontracker.services.implementation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.trix.wowgarrisontracker.converters.AccountCharacterPojoToAccountCharacter;
import com.trix.wowgarrisontracker.model.AccountCharacter;
import com.trix.wowgarrisontracker.pojos.AccCharacterResourcesPojo;
import com.trix.wowgarrisontracker.pojos.AccountCharacterPojo;
import com.trix.wowgarrisontracker.repository.AccountCharacterRepository;
import com.trix.wowgarrisontracker.repository.EntryRepository;
import com.trix.wowgarrisontracker.services.interfaces.AccountCharacterService;

import org.springframework.stereotype.Service;

@Service
public class AccountCharacterServiceImpl implements AccountCharacterService {

	private AccountCharacterRepository accountCharacterRepository;
	private EntryRepository entryRepository;
	private AccountCharacterPojoToAccountCharacter accountCharacterPojoToAccountCharacter;

	public AccountCharacterServiceImpl(AccountCharacterRepository accountCharacterRepository,
			EntryRepository entryRepository,
			AccountCharacterPojoToAccountCharacter accountCharacterPojoToAccountCharacter) {
		this.accountCharacterRepository = accountCharacterRepository;
		this.entryRepository = entryRepository;
		this.accountCharacterPojoToAccountCharacter = accountCharacterPojoToAccountCharacter;
	}

	@Override
	public AccountCharacter findById(Long id) {
		Optional<AccountCharacter> optionalAccountCharacter = accountCharacterRepository.findById(id);
		return optionalAccountCharacter.isPresent() ? optionalAccountCharacter.get() : null;
	}

	@Override
	public List<AccountCharacter> listOfAccountCharacters(Long accountId) {
		return accountCharacterRepository.findAllByAccountId(accountId);
	}

	@Override
	public List<AccCharacterResourcesPojo> listOfResources(Long accountId) {

		List<AccCharacterResourcesPojo> accResourcerList = new ArrayList<>();

		for (AccountCharacter tmpAccCharacter : this.listOfAccountCharacters(accountId)) {
			AccCharacterResourcesPojo accCharacterResourcesPojo = new AccCharacterResourcesPojo();
			accCharacterResourcesPojo.setCharacterName(tmpAccCharacter.getCharacterName());
			accCharacterResourcesPojo.setWarPaint(entryRepository.getWarPaintByCharacterId(tmpAccCharacter.getId()));
			accCharacterResourcesPojo
					.setGarrisonResources(entryRepository.getGarrisonResourcesByCharacterId(tmpAccCharacter.getId()));
			accCharacterResourcesPojo.setId(tmpAccCharacter.getId());
			accResourcerList.add(accCharacterResourcesPojo);
		}

		return accResourcerList;
	}

	@Override
	public boolean isNameTaken(Long accountId, String name) {

		Optional<AccountCharacter> accountCharacterOptional = this.listOfAccountCharacters(accountId).stream()
				.filter(tmp -> tmp.getCharacterName().equals(name)).findFirst();

		return accountCharacterOptional.isPresent();
	}

	@Override
	public void save(AccountCharacterPojo characterPojo) {

		AccountCharacter accountCharacter = accountCharacterPojoToAccountCharacter.convert(characterPojo);

		accountCharacterRepository.save(accountCharacter);

	}

}
