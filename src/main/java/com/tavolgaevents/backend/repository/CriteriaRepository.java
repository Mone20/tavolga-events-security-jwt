package com.tavolgaevents.backend.repository;

import com.tavolgaevents.backend.models.Criterion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CriteriaRepository extends CrudRepository<Criterion, Long> {
    List<Criterion> findByIdIn(List<Long> longIds);
}
