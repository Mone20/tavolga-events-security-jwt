package com.tavolgaevents.backend.seeders;

import com.tavolgaevents.backend.models.*;
import com.tavolgaevents.backend.payload.request.SignupRequest;
import com.tavolgaevents.backend.repository.*;
import com.tavolgaevents.backend.services.impl.AuthServiceImpl;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.util.Collections;
import java.util.List;

@Component
public class DataSeeders {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private ContestPartTypeRepository contestPartTypeRepository;
    private ContestPartRepository contestPartRepository;
    private ContestRepository contestRepository;
    private NominationRepository nominationRepository;
    private CriteriaRepository criteriaRepository;
    private AuthServiceImpl authService;

    @Autowired
    public DataSeeders(
            UserRepository userRepository,
            RoleRepository roleRepository,
            ContestPartTypeRepository contestPartTypeRepository,
            ContestPartRepository contestPartRepository,
            ContestRepository contestRepository,
            NominationRepository nominationRepository,
            CriteriaRepository criteriaRepository,
            AuthServiceImpl authService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.contestPartTypeRepository = contestPartTypeRepository;
        this.authService = authService;
        this.criteriaRepository = criteriaRepository;
        this.nominationRepository = nominationRepository;
        this.contestRepository = contestRepository;
        this.contestPartRepository = contestPartRepository;
    }

    @EventListener
    public void seed(ContextRefreshedEvent event) {
        seedRoleTable();
        seedUserTable();
        seedContestPartTypeTable();
        List<Contest> contests = seedContestTable();
        List<Nomination> nominations = seedNominationTable(contests);
        seedCriterionTable(nominations);
        seedContestPartTable(contests);
    }

    private void seedRoleTable() {
       if(!roleRepository.existsByName(RoleConstants.ROLE_USER))
            roleRepository.save(new Role(RoleConstants.ROLE_USER));
        if(!roleRepository.existsByName(RoleConstants.ROLE_ADMIN))
            roleRepository.save(new Role(RoleConstants.ROLE_ADMIN));
        if(!roleRepository.existsByName(RoleConstants.ROLE_ASSESSOR))
            roleRepository.save(new Role(RoleConstants.ROLE_ASSESSOR));
        if(!roleRepository.existsByName(RoleConstants.ROLE_JURY))
            roleRepository.save(new Role(RoleConstants.ROLE_JURY));
    }

    private List<Contest> seedContestTable() {
        List<Contest> contests = ((List<Contest>)contestRepository.findAll());
        if(contests.isEmpty()) {
            Contest contest1 = new Contest();
            contest1.setName("Contest 1");
            contest1.setDescription("Description Contest 1");

            contests.add(contestRepository.save(contest1));

            Contest contest2 = new Contest();
            contest2.setName("Contest 2");
            contest2.setDescription("Description Contest 2");

            contests.add(contestRepository.save(contest2));

            Contest contest3 = new Contest();
            contest3.setName("Contest 3");
            contest3.setDescription("Description Contest 3");

            contests.add(contestRepository.save(contest3));

        }
        return contests;
    }

    private List<Nomination> seedNominationTable(List<Contest> contests) {
        List<Nomination> nominations = ((List<Nomination>)nominationRepository.findAll());
        if(nominations.isEmpty()) {
            User assessor = userRepository.findByUsername("assessor").get();
            User assessor1 = userRepository.findByUsername("assessor1").get();
            User user = userRepository.findByUsername("user").get();
            User user1 = userRepository.findByUsername("user1").get();
            Nomination nomination1 = new Nomination();
            nomination1.setName("Nomination 1");
            nomination1.setDescription("Description Nomination 1");
            nomination1.setParticipants(Collections.singletonList(userRepository.findByUsername("user").get()));
            nomination1.setRatingUsers(Collections.singletonList(userRepository.findByUsername("assessor").get()));

            nomination1.setContests(Collections.singletonList(contests.get(0)));
            contests.get(0).setNominations(Collections.singletonList(nomination1));
            nominations.add(nominationRepository.save(nomination1));
            user.setNominations(Collections.singletonList(nomination1));
            assessor.setRateNominations(Collections.singletonList(nomination1));
            userRepository.save(user);
            userRepository.save(assessor);
            Nomination nomination2 = new Nomination();
            nomination2.setName("Nomination 2");
            nomination2.setDescription("Description Nomination 2");
            nomination2.setParticipants(Collections.singletonList(userRepository.findByUsername("user1").get()));
            nomination2.setRatingUsers(Collections.singletonList(userRepository.findByUsername("assessor1").get()));
            nomination2.setContests(Collections.singletonList(contests.get(1)));
            nominations.add(nominationRepository.save(nomination2));

            user1.setNominations(Collections.singletonList(nomination2));
            assessor1.setRateNominations(Collections.singletonList(nomination2));
            contests.get(1).setNominations(Collections.singletonList(nomination2));
            contestRepository.saveAll(contests);
            userRepository.save(user1);
            userRepository.save(assessor1);
        }
        return nominations;
    }

    private void seedCriterionTable(List<Nomination> nominations) {
        List<Criterion> criteria = ((List<Criterion>)criteriaRepository.findAll());
        if(criteria.isEmpty()) {
            Criterion criterion1 = new Criterion();
            criterion1.setName("Criterion 1");
            criterion1.setDescription("Description Criterion 1");
            criterion1.setMaxAssessment(10);
            criterion1.setNominations(Collections.singletonList(nominations.get(0)));
            criteria.add(criteriaRepository.save(criterion1));
            nominations.get(0).setCriterionList(Collections.singletonList(criterion1));


            Criterion criterion2 = new Criterion();
            criterion2.setName("Criterion 2");
            criterion2.setDescription("Description Criterion 2");
            criterion2.setMaxAssessment(10);
            criterion2.setNominations(Collections.singletonList(nominations.get(1)));
            criteria.add(criteriaRepository.save(criterion2));
            nominations.get(1).setCriterionList(Collections.singletonList(criterion2));
            nominationRepository.saveAll(nominations);


        }
    }



    private void seedUserTable() {
        if(((List<User>)userRepository.findAll()).isEmpty()) {
            SignupRequest request = new SignupRequest();
            request.setUsername("admin");
            request.setEmail("admin@test.com");
            request.setFirstName("Admin");
            request.setLastName("Adminov");
            request.setMiddleName("Adminovich");
            request.setPassword("tavolgaTestPassword");
            request.setRole(RoleConstants.ROLE_ADMIN);
            authService.register(request);

            request.setUsername("user");
            request.setEmail("user@test.com");
            request.setFirstName("User");
            request.setLastName("Userov");
            request.setMiddleName("Userovich");
            request.setPassword("tavolgaTestPassword");
            request.setRole(RoleConstants.ROLE_USER);
            authService.register(request);

            request.setUsername("user1");
            request.setEmail("use1r@test.com");
            request.setFirstName("User1");
            request.setLastName("Userov1");
            request.setMiddleName("Userovich1");
            request.setPassword("tavolgaTestPassword");
            request.setRole(RoleConstants.ROLE_USER);
            authService.register(request);

            request.setUsername("assessor");
            request.setEmail("assessor@test.com");
            request.setFirstName("assessor");
            request.setLastName("assessor");
            request.setMiddleName("assessor");
            request.setPassword("tavolgaTestPassword");
            request.setRole(RoleConstants.ROLE_ASSESSOR);
            authService.register(request);

            request.setUsername("assessor1");
            request.setEmail("assessor1@test.com");
            request.setFirstName("assessor1");
            request.setLastName("assessor1");
            request.setMiddleName("assessor1");
            request.setPassword("tavolgaTestPassword");
            request.setRole(RoleConstants.ROLE_ASSESSOR);
            authService.register(request);

            request.setUsername("jury");
            request.setEmail("jury@test.com");
            request.setFirstName("jury");
            request.setLastName("jury");
            request.setMiddleName("jury");
            request.setPassword("tavolgaTestPassword");
            request.setRole(RoleConstants.ROLE_JURY);
            authService.register(request);
        }

    }

    private void seedContestPartTable(List<Contest> contests) {
        if(((List<ContestPart>)contestPartRepository.findAll()).isEmpty()) {
            for (Contest contest: contests) {
                ContestPart contestAssessorPart = new ContestPart();
                contestAssessorPart.setAccessRole(RoleConstants.ROLE_ASSESSOR);
                contestAssessorPart.setStartDate(new Date(DateTime.now().getMillis()));
                contestAssessorPart.setEndDate(new Date(DateTime.now().plusDays(2).getMillis()));
                contestAssessorPart.setContestPartType(contestPartTypeRepository.findAll().iterator().next());
                contestAssessorPart.setContest(contest);
                contestPartRepository.save(contestAssessorPart);

                ContestPart contestAssessorPart1 = new ContestPart();
                contestAssessorPart1.setAccessRole(RoleConstants.ROLE_JURY);
                contestAssessorPart1.setStartDate(new Date(DateTime.now().getMillis()));
                contestAssessorPart1.setEndDate(new Date(DateTime.now().plusDays(2).getMillis()));
                contestAssessorPart1.setContestPartType(contestPartTypeRepository.findAll().iterator().next());
                contestAssessorPart1.setContest(contest);
                contestPartRepository.save(contestAssessorPart1);

                ContestPart contestAssessorPart2 = new ContestPart();
                contestAssessorPart2.setAccessRole(RoleConstants.ROLE_USER);
                contestAssessorPart2.setStartDate(new Date(DateTime.now().getMillis()));
                contestAssessorPart2.setEndDate(new Date(DateTime.now().plusDays(2).getMillis()));
                contestAssessorPart2.setContestPartType(contestPartTypeRepository.findAll().iterator().next());
                contestAssessorPart2.setContest(contest);
                contestPartRepository.save(contestAssessorPart2);
            }
        }
    }

    private void seedContestPartTypeTable() {
        if(((List<ContestPartType>)contestPartTypeRepository.findAll()).isEmpty()) {
            contestPartTypeRepository.save(new ContestPartType("Очный этап"));
            contestPartTypeRepository.save(new ContestPartType("Заочный этап"));
            contestPartTypeRepository.save(new ContestPartType("Подведение итогов"));
            contestPartTypeRepository.save(new ContestPartType("Регистрация"));

        }
    }



}
