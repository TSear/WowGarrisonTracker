package com.trix.wowgarrisontracker.repository;

import java.util.Optional;

import com.trix.wowgarrisontracker.model.Account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

	Optional<Account> findByLogin(String login);
}