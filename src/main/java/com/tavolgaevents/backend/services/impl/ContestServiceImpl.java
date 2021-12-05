package com.tavolgaevents.backend.services.impl;

import com.tavolgaevents.backend.models.Contest;
import com.tavolgaevents.backend.models.Nomination;
import com.tavolgaevents.backend.payload.request.ContestRequest;
import com.tavolgaevents.backend.models.User;
import com.tavolgaevents.backend.repository.ContestRepository;
import com.tavolgaevents.backend.repository.NominationRepository;
import com.tavolgaevents.backend.services.ContestService;
import com.tavolgaevents.backend.services.UserService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
