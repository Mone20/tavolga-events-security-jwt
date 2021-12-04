package com.tavolgaevents.backend.repository;

import com.tavolgaevents.backend.models.Assessment;
import com.tavolgaevents.backend.models.Contest;
import com.tavolgaevents.backend.models.Criterion;
import com.tavolgaevents.backend.models.Nomination;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AssessmentRepository extends CrudRepository<Assessment, Long> {
    public List<Assessment> findByContestAndNominationAndCriterion(Contest contest, Nomination nomination, Criterion criterion);
}
