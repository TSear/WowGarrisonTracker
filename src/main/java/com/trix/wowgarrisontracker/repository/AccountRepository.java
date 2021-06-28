package com.trix.wowgarrisontracker.repository;

import com.trix.wowgarrisontracker.model.Account;
import com.trix.wowgarrisontracker.model.Entry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByLogin(String login);

    boolean existsByLogin(String login);

    Optional<Account> findByVerificationCode(String verificationCode);

    @Query("SELECT sum(entry.garrisonResources) FROM Account acc " +
            "INNER JOIN acc.accountCharacters accChars " +
            "INNER JOIN accChars.entries entry " +
            "WHERE acc.id = :accountId")
    Long sumEntriesGarrisonResourcesByAccountId(Long accountId);

    @Query("SELECT sum(entry.warPaint) FROM Account acc " +
            "INNER JOIN acc.accountCharacters accChars " +
            "INNER JOIN accChars.entries entry " +
            "WHERE acc.id = :accountId")
    Long sumEntriesWarPaintByAccountId(Long accountId);

    @Query("SELECT count(entry.id) FROM Account acc " +
            "INNER JOIN acc.accountCharacters accChars " +
            "INNER JOIN accChars.entries entry " +
            "WHERE acc.id = :accountId")
    Long countEntriesByAccountId(Long accountId);

    @Query("SELECT count(DISTINCT entry.entryDate) FROM Account acc " +
            "INNER JOIN acc.accountCharacters accChars " +
            "INNER JOIN accChars.entries entry " +
            "WHERE acc.id = :accountId")
    Long countDaysByAccountId(Long accountId);

}