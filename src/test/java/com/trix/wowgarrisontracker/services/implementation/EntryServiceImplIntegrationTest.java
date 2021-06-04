package com.trix.wowgarrisontracker.services.implementation;

import com.trix.wowgarrisontracker.TestUtils;
import com.trix.wowgarrisontracker.model.AccountCharacter;
import com.trix.wowgarrisontracker.model.Entry;
import com.trix.wowgarrisontracker.repository.AccountCharacterRepository;
import com.trix.wowgarrisontracker.repository.EntryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class EntryServiceImplIntegrationTest {

    @Autowired
    EntryRepository entryRepository;

    @Autowired
    AccountCharacterRepository accountCharacterRepository;

    List<Entry> entries;
    AccountCharacter accountCharacter;

    @BeforeEach
    void setUp() {

        accountCharacter = new AccountCharacter();
        accountCharacter.setId(1L);

        entries = Arrays.asList(
                TestUtils.entryGenerator(20, 40, accountCharacter),
                TestUtils.entryGenerator(50, 30, accountCharacter),
                TestUtils.entryGenerator(150, 10, accountCharacter)
        );


    }

    @Test
    @Rollback(value = true)
    void save() {
        //given


        //when
        accountCharacterRepository.save(accountCharacter);

        //then
        assertEquals(entries.size(), entryRepository.count());
    }

    @Test
    @Rollback(value = true)
    void findAllByAccountCharacterId() {
        //given

        //when
        accountCharacterRepository.save(accountCharacter);

        //then
        assertEquals(entries.size(), entryRepository.findAllByAccountCharacterId(accountCharacter.getId()).size());
    }
}
