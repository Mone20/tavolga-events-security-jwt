package com.tavolgaevents.backend.services;

import com.tavolgaevents.backend.models.Contest;
import com.tavolgaevents.backend.models.User;
import com.tavolgaevents.backend.payload.request.ContestRequest;

import java.util.List;


public interface ContestService {


    List<Contest> getContestByUserId(Long userId, boolean isRateUsers);

    List<User> getRatingByContentId(Long contentId);

    Contest create(ContestRequest request);

    Contest update(Long id, ContestRequest request);

    void delete(Long id);
}
