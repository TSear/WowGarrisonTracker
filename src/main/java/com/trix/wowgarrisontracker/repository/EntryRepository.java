package com.trix.wowgarrisontracker.repository;

import java.util.List;

import com.trix.wowgarrisontracker.model.Entry;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EntryRepository extends JpaRepository<Entry, Long>{
    
    @Query("SELECT SUM(e.warPaint) FROM Entry e WHERE e.accountCharacterId.id = :id")
    public Long getWarPaintByCharacterId(@Param("id")Long id);

    @Query("SELECT SUM(e.garrisonResources) FROM Entry e WHERE e.accountCharacterId.id = :id")
    public Long getGarrisonResourcesByCharacterId(@Param("id")Long id);

}
