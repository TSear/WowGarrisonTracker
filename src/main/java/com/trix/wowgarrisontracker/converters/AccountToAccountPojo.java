package com.trix.wowgarrisontracker.converters;

import java.util.HashSet;
import java.util.Set;

import com.trix.wowgarrisontracker.model.Account;
import com.trix.wowgarrisontracker.pojos.AccountCharacterPojo;
import com.trix.wowgarrisontracker.pojos.AccountPojo;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class AccountToAccountPojo implements Converter<Account, AccountPojo>{

    private AccountCharacterToAccountCharacterPojo accountCharacterConverter;
    
    /**
	 * @param accountCharacterConverter
	 */
	public AccountToAccountPojo(AccountCharacterToAccountCharacterPojo accountCharacterConverter) {
		super();
		this.accountCharacterConverter = accountCharacterConverter;
	}



	@Override
    public AccountPojo convert(Account source) {
        AccountPojo accountPojo = new AccountPojo();

        accountPojo.setId(source.getId());
        accountPojo.setLogin(source.getLogin());
        accountPojo.setPassword(source.getPassword());
        accountPojo.setGarrisonResources(source.getTotalGarrisonResources());
        accountPojo.setAmountOfEntries((long) source.getAmountOfEntries());
        accountPojo.setWarPaint(source.getTotalWarPaint());
        Set<AccountCharacterPojo> accountCharacterPojos = new HashSet<>();
        
        accountPojo.setAccountCharacters(accountCharacterPojos);

        return accountPojo;
    }
    
}
