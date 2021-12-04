package com.tavolgaevents.backend.seeders;

import com.tavolgaevents.backend.models.ContestPartType;
import com.tavolgaevents.backend.models.Role;
import com.tavolgaevents.backend.models.RoleConstants;
import com.tavolgaevents.backend.models.User;
import com.tavolgaevents.backend.payload.request.SignupRequest;
import com.tavolgaevents.backend.repository.ContestPartTypeRepository;
import com.tavolgaevents.backend.repository.RoleRepository;
import com.tavolgaevents.backend.repository.UserRepository;
import com.tavolgaevents.backend.services.impl.AuthServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataSeeders {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private ContestPartTypeRepository contestPartTypeRepository;
    private AuthServiceImpl authService;

    @Autowired
    public DataSeeders(
            UserRepository userRepository,
            RoleRepository roleRepository,
            ContestPartTypeRepository contestPartTypeRepository,
            AuthServiceImpl authService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.contestPartTypeRepository = contestPartTypeRepository;
        this.authService = authService;
    }

    @EventListener
    public void seed(ContextRefreshedEvent event) {
        seedRoleTable();
        seedUserTable();
        seedContestPartTypeTable();
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
