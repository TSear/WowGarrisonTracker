package com.trix.wowgarrisontracker.repository;

import com.trix.wowgarrisontracker.model.Entry;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EntryRepository extends JpaRepository<Entry, Long> {

    @Query(value = "SELECT COALESCE(SUM(e.warPaint),0) FROM Entry e WHERE e.accountCharacter.id= :id")
    Long getWarPaintByCharacterId(@Param("id") Long id);

    @Query("SELECT COALESCE(SUM(e.garrisonResources),0) FROM Entry e WHERE e.accountCharacter.id= :id")
    Long getGarrisonResourcesByCharacterId(@Param("id") Long id);

    @Query("SELECT e FROM Entry e WHERE e.accountCharacter.id = :accountCharacterId")
    List<Entry> findAllByAccountCharacterId(@Param("accountCharacterId") Long accountCharacterId);

    @Query("SELECT COUNT(entries.id) FROM Account a JOIN a.accountCharacters accChars JOIN accChars.entries entries WHERE a.id = :accountId GROUP BY entries.entryDate ")
    List<Long> getListOfEntriesEachDay(@Param("accountId") Long accountId);

    @Query("SELECT entries FROM Account acc JOIN acc.accountCharacters accChars JOIN accChars.entries entries WHERE acc.id = :accountId ORDER BY entries.entryDate DESC")
    List<Entry> findAllEntriesByAccountId(Long accountId, Pageable pageable);

    @Query("SELECT count(entries.id) FROM Account acc JOIN acc.accountCharacters accChars JOIN accChars.entries entries WHERE acc.id = :accountId")
    int getAmountOfEntries(Long accountId);


}
