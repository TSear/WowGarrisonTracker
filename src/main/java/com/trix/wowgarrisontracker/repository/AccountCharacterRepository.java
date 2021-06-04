package com.trix.wowgarrisontracker.repository;

import com.trix.wowgarrisontracker.model.AccountCharacter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountCharacterRepository extends JpaRepository<AccountCharacter, Long> {

    List<AccountCharacter> findAllByAccountId(Long accountId);

    Optional<AccountCharacter> findAccountCharacterByCharacterName(String name);

}
