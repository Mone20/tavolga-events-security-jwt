package com.tavolgaevents.backend.repository;

import com.tavolgaevents.backend.models.Nomination;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface NominationRepository extends CrudRepository<Nomination, Long> {
    List<Nomination> findByIdIn(List<Long> longIds);
}
