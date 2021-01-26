package com.trix.wowgarrisontracker.repository;

import com.trix.wowgarrisontracker.model.AccountCharacter;
import com.trix.wowgarrisontracker.pojos.AccCharacterResourcesPojo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountCharacterRepository extends JpaRepository<AccountCharacter, Long> {


}
