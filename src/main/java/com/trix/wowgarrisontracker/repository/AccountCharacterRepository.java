package com.trix.wowgarrisontracker.repository;

import com.trix.wowgarrisontracker.model.AccountCharacter;
import com.trix.wowgarrisontracker.pojos.AccCharacterResourcesPojo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface AccountCharacterRepository extends JpaRepository<AccountCharacter, Long> {

	List<AccountCharacter> findAllByAccountId(Long accountId);

	@Transactional
	@Modifying
	@Query("Update AccountCharacter acc set acc.garrisonResources = :garrisonResources, acc.warPaint = :warPaint where acc.id = :id")
	void updateGarrisonResourcesAndWarPaint(Long id, Long garrisonResources, Long warPaint);
	Optional<AccountCharacter> findAccountCharacterByCharacterName(String name);
}
