package com.trix.wowgarrisontracker.repository;

import com.trix.wowgarrisontracker.model.Options;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OptionsRepository extends JpaRepository<Options, Long> {

    boolean updateOptions(Options options);
}
