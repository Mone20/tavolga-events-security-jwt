package com.tavolgaevents.backend.repository;

import com.tavolgaevents.backend.models.ContestPart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface ContestPartRepository extends CrudRepository<ContestPart, Long> {
}
