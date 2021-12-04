package com.tavolgaevents.backend.services.impl;

import com.tavolgaevents.backend.models.Contest;
import com.tavolgaevents.backend.models.Nomination;
import com.tavolgaevents.backend.payload.request.NominationRequest;
import com.tavolgaevents.backend.repository.ContestRepository;
import com.tavolgaevents.backend.repository.CriteriaRepository;
import com.tavolgaevents.backend.repository.NominationRepository;
import com.tavolgaevents.backend.services.NominationService;
import com.tavolgaevents.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NominationServiceImpl implements NominationService {
    

    @Autowired
    private CriteriaRepository criteriaRepository;

    @Autowired
    private NominationRepository nominationRepository;

    @Autowired
    private ContestRepository contestRepository;
    @Autowired
    private UserService userService;


    @Override
    public Nomination create(NominationRequest request) {
        Nomination nomination = new Nomination();
        return update(nomination, request);
    }

    private Nomination update(Nomination nomination, NominationRequest request) {
        nomination.setDescription(request.description);
        nomination.setName(request.name);
        if(!CollectionUtils.isEmpty(request.criteriaList)) {
            List<Long> longIds = request.criteriaList.stream().map(Long::parseLong).collect(Collectors.toList());
            nomination.setCriterionList(criteriaRepository.findByIdIn(longIds));
        }
        if (!StringUtils.isEmpty(request.contestId)) {
            Contest contest = contestRepository.findById(Long.parseLong(request.contestId)).get();
            if(nomination.getContests().isEmpty())
               nomination.setContests(Collections.singletonList(contest));
            else
                nomination.getContests().add(contest);
        }
        nomination = nominationRepository.save(nomination);
        return nomination;
    }

    @Override
    public Nomination update(Long id, NominationRequest request) {
        Nomination nomination = nominationRepository.findById(id).get();
        return update(nomination, request);
    }

    @Override
    public void delete(Long id) {
        nominationRepository.deleteById(id);
    }

    @Override
    public List<Nomination> getNominationByUserId(long id) {
        return userService.getUserById(id).getNominations();
    }

    @Override
    public List<Nomination> getNominationByAssessorId(long id) {
        return userService.getUserById(id).getRateNominations();
    }

    @Override
    public List<Nomination> filterByContestId(long id, List<Nomination> nominations) {
        return nominations
                .stream()
                .filter(nomination ->
                        nomination.getContests()
                                .stream().anyMatch(contest -> contest.getId().equals(Long.valueOf(id)))
                ).collect(Collectors.toList());
    }
}
