package com.trix.wowgarrisontracker.converters;

import com.trix.wowgarrisontracker.model.AccountCharacter;
import com.trix.wowgarrisontracker.pojos.AccountCharacterPojo;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class AccountCharacterToAccountCharacterPojo implements Converter<AccountCharacter, AccountCharacterPojo> {

    public AccountCharacterToAccountCharacterPojo() {
        super();
    }

    @Override
    public AccountCharacterPojo convert(AccountCharacter source) {
        AccountCharacterPojo accountCharacterPojo = new AccountCharacterPojo();
        accountCharacterPojo.setAccountId(source.getAccount().getId());
        accountCharacterPojo.setCharacterName(source.getCharacterName());
        accountCharacterPojo.setId(source.getId());
        accountCharacterPojo.setGarrisonResources(source.getGarrisonResources());
        accountCharacterPojo.setWarPaint(source.getWarPaint());
        return accountCharacterPojo;
    }

}
