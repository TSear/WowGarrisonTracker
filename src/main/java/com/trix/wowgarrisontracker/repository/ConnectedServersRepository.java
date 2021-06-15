package com.trix.wowgarrisontracker.repository;

import com.trix.wowgarrisontracker.model.ConnectedServersModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConnectedServersRepository extends JpaRepository<ConnectedServersModel, Integer> {

}
