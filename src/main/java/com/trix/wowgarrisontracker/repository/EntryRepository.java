package com.trix.wowgarrisontracker.repository;

import java.util.List;

import com.trix.wowgarrisontracker.model.Entry;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EntryRepository extends JpaRepository<Entry, Long>{
    
    @Query(value = "SELECT COALESCE(SUM(e.warPaint),0) FROM Entry e WHERE e.accountCharacter.id= :id")
    public Long getWarPaintByCharacterId(@Param("id")Long id);

    @Query("SELECT COALESCE(SUM(e.garrisonResources),0) FROM Entry e WHERE e.accountCharacter.id= :id")
    public Long getGarrisonResourcesByCharacterId(@Param("id")Long id);

    @Query("SELECT e FROM Entry e WHERE e.accountCharacter.id = :accountCharacterId")
    public List<Entry>	findByAccountCharacterId(@Param("accountCharacterId")Long accountCharacterId);

    @Query("SELECT COUNT(entries.id) FROM Account a JOIN a.accountCharacters accChars JOIN accChars.entries entries WHERE a.id = :accountId GROUP BY entries.entryDate ")
    public List<Long> getListOfEntriesEachDay(@Param("accountId")Long accountId);
}
