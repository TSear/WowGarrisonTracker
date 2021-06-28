package com.trix.wowgarrisontracker.services.implementation;

import com.trix.wowgarrisontracker.TestUtils;
import com.trix.wowgarrisontracker.model.AccountCharacter;
import com.trix.wowgarrisontracker.model.Entry;
import com.trix.wowgarrisontracker.repository.AccountCharacterRepository;
import com.trix.wowgarrisontracker.repository.EntryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.trix.wowgarrisontracker.TestUtils.generateEntryNoCharacter;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class EntryServiceImplTest {

    @Mock
    EntryRepository entryRepository;

    @Mock
    AccountCharacterRepository accountCharacterRepository;

    List<Entry> entries;
    AccountCharacter accountCharacter;

    @InjectMocks
    EntryServiceImpl entryService;


    @BeforeEach
    void setUp() {


        accountCharacter = new AccountCharacter();
        accountCharacter.setId(1L);

        entries = Arrays.asList(
                TestUtils.generateEntryWithCharacter(20, 40, accountCharacter),
                TestUtils.generateEntryWithCharacter(50, 30, accountCharacter),
                TestUtils.generateEntryWithCharacter(150, 10, accountCharacter)
        );


    }

    @Test
    void saveAll() {
        //given
        List<Entry> entriesNullCharacter = Arrays.asList(
                generateEntryNoCharacter(50, 10),
                generateEntryNoCharacter(50, 20),
                generateEntryNoCharacter(100, 40));

        //when
        when(entryRepository.saveAll(entriesNullCharacter)).thenReturn(entriesNullCharacter);

        //then
        assertTrue(entryService.saveAll(entriesNullCharacter));
    }

    @Test
    void save_Null() {
        //given

        Entry entry = null;

        //when
        when(entryRepository.save(Mockito.any(Entry.class))).thenReturn(new Entry());

        //then
        assertNull(entryService.save(entry));
    }

    @Test
    void save_NotNull() {
        //given

        Entry entry = generateEntryNoCharacter(10, 20);

        //when
        when(entryRepository.save(entry)).thenReturn(entry);

        //then
        assertNotNull(entryService.save(entry));
    }

    @Test
    @Rollback()
    void findAllByAccountCharacterId() {
        //given


        //when
        when(entryRepository.findAllByAccountCharacterId(Mockito.anyLong())).thenReturn(entries);

        //then
        assertEquals(entries.size(), entryService.findAllByAccountCharacterId(1L).size());
    }

    @Test
    public void findById_NotFound() {

        //given

        //when
        when(entryRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        //then
        assertThrows(RuntimeException.class, () -> entryService.findById(1L));
    }

    @Test
    void findById_found() {
        //given
        Entry entry = new Entry();

        //when
        when(entryRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(entry));

        //then
        assertEquals(entry,entryService.findById(1L));
    }
}
