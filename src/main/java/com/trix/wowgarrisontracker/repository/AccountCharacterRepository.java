package com.trix.wowgarrisontracker.repository;

import com.trix.wowgarrisontracker.model.AccountCharacter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountCharacterRepository extends JpaRepository<AccountCharacter, Long> {

    List<AccountCharacter> findAllByAccountId(Long accountId);

    Optional<AccountCharacter> findAccountCharacterByCharacterName(String name);


    @Query("SELECT accountCharacters FROM AccountCharacter accountCharacters where accountCharacters.account.id = :id")
    List<AccountCharacter> findAllAccountCharactersByAccountPaged(Long id, Pageable pageable);

    @Query("SELECT count(accountCharacters.id) FROM AccountCharacter accountCharacters  WHERE accountCharacters.account.id = :id")
    int countAccountCharacterByAccountId(Long id);

    @Query("Select sum(entry.garrisonResources) FROM AccountCharacter acc INNER JOIN acc.entries entry WHERE acc.id = :accountCharacterId")
    Long sumGarrisonResourcesByAccountCharacterId(AccountCharacter accountCharacterId);

    @Query("Select sum(entry.warPaint) FROM AccountCharacter acc INNER JOIN acc.entries entry WHERE acc.id = :accountCharacterId")
    Long sumWarPaintByAccountCharacterId(AccountCharacter accountCharacterId);
}
