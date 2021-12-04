package com.tavolgaevents.backend.services;

import com.tavolgaevents.backend.models.Criterion;
import com.tavolgaevents.backend.payload.request.CriterionRequest;

import java.util.List;

public interface CriterionService {


    Criterion create(CriterionRequest request);

    Criterion update(Long id, CriterionRequest request);

    void delete(Long id);

}
