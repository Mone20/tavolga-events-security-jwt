package com.tavolgaevents.backend.repository;

import com.tavolgaevents.backend.models.ContestPartType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface ContestPartTypeRepository extends CrudRepository<ContestPartType, Long> {
}
