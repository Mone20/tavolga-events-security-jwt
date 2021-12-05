package com.tavolgaevents.backend.services.impl;

import com.tavolgaevents.backend.models.*;
import com.tavolgaevents.backend.payload.request.ContestRequest;
import com.tavolgaevents.backend.repository.ContestRepository;
import com.tavolgaevents.backend.repository.NominationRepository;
import com.tavolgaevents.backend.services.ContestService;
import com.tavolgaevents.backend.services.UserService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class ContestServiceImpl implements ContestService {

    @Autowired
    private UserService userService;

    @Autowired
    private NominationRepository nominationRepository;

    @Autowired
    private ContestRepository contestRepository;

    @Override
    public List<Contest> getContestByUserId(Long userId, boolean isRateUsers) {
        User user = userService.getUserById(userId);
        List<Contest> contests = new ArrayList<>();
        for(Nomination nomination: !isRateUsers ? user.getNominations() : user.getRateNominations()) {
            contests.addAll(nomination.getContests());
        }
        contests = contests.stream()
                .filter(contest -> contest.getContestParts().stream()
                .anyMatch(contestPart -> contestPart.startDate.before(DateTime.now().toDate())
                        && contestPart.getAccessRole().equals(user.getRole().getName())
                        && contestPart.endDate.after(DateTime.now().toDate())))
                .collect(Collectors.toList());
        return contests;
    }

    @Override
    public List<User> getRatingByContentId(Long contentId) {
        List<User> users = userService.getAllByContest(contentId);
        List<UserRate> userRates = new ArrayList<>();
        for(User user: users) {
            int sum = 0;
            for(Nomination nomination: user.getNominations()) {
                for (Criterion criterion : nomination.getCriterionList()) {
                    List<Integer> assessmentList = criterion.getAssessments().stream().map(Assessment::getAssessment).collect(Collectors.toList());
                    for(Integer assessment : assessmentList) {
                        sum += assessment;
                    }
                }
            }
            userRates.add(new UserRate(sum, user));
        }
        return userRates.stream().sorted(Comparator.comparingInt(UserRate::getResult)).map(UserRate::getUser).collect(Collectors.toList());
    }

    @Override
    public Contest create(ContestRequest request) {
        Contest contest = new Contest();
        return update(contest, request);
    }

    private Contest update(Contest contest, ContestRequest request) {
        contest.setDescription(request.description);
        contest.setName(request.name);
        if(!CollectionUtils.isEmpty(request.nominations)) {
            List<Long> longIds = request.nominations.stream().map(Long::parseLong).collect(Collectors.toList());
            if(!contest.getNominations().isEmpty())
                contest.getNominations().addAll(nominationRepository.findByIdIn(longIds));
            else
                contest.setNominations(nominationRepository.findByIdIn(longIds));
        }
        contest = contestRepository.save(contest);
        return contest;

    }

    @Override
    public Contest update(Long id, ContestRequest request) {
        Contest contest = contestRepository.findById(id).get();
        return update(contest, request);
    }

    @Override
    public void delete(Long id) {
        contestRepository.deleteById(id);
    }

}
