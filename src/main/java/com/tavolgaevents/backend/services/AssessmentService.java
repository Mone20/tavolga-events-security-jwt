package com.tavolgaevents.backend.services;

import com.tavolgaevents.backend.models.Assessment;
import com.tavolgaevents.backend.payload.request.AssessmentRequest;

public interface AssessmentService {
    Assessment create(AssessmentRequest request);

    Assessment update(Long id, AssessmentRequest request);

    void delete(Long id);
}
