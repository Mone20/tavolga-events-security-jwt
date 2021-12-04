package com.tavolgaevents.backend.services;

import com.tavolgaevents.backend.models.Nomination;
import com.tavolgaevents.backend.payload.request.NominationRequest;

import java.util.List;

public interface NominationService {

    Nomination create(NominationRequest request);


    Nomination update(Long id, NominationRequest request);

    void delete(Long id);

    List<Nomination> getNominationByUserId(long id);

    List<Nomination> getNominationByAssessorId(long id);

    List<Nomination> filterByContestId(long id, List<Nomination> nominations);
}
