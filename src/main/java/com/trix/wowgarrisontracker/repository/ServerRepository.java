package com.trix.wowgarrisontracker.repository;

import com.trix.wowgarrisontracker.model.Server;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServerRepository extends JpaRepository<Server, Long> {

}
