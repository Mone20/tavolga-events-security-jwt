package com.tavolgaevents.backend.services.impl;

import com.tavolgaevents.backend.models.File;
import com.tavolgaevents.backend.models.Role;
import com.tavolgaevents.backend.models.RoleConstants;
import com.tavolgaevents.backend.models.User;
import com.tavolgaevents.backend.payload.request.SignupRequest;
import com.tavolgaevents.backend.repository.RoleRepository;
import com.tavolgaevents.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthServiceImpl {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    public JavaMailSender emailSender;

    public void register(SignupRequest signUpRequest) {
        User user = new User(signUpRequest.getUsername(), signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        user.setFirstName(signUpRequest.getFirstName());
        user.setLastName(signUpRequest.getLastName());
        user.setMiddleName(signUpRequest.getMiddleName());
        String strRole = signUpRequest.getRole();
        Optional<Role> userRole = Optional.empty();

        if (strRole == null) {
            userRole = roleRepository.findByName(RoleConstants.ROLE_USER);
        } else {
            switch (strRole) {
                case RoleConstants.ROLE_ADMIN:
                    userRole = roleRepository.findByName(RoleConstants.ROLE_ADMIN);
                    break;
                case RoleConstants.ROLE_ASSESSOR:
                    userRole = roleRepository.findByName(RoleConstants.ROLE_ASSESSOR);
                    break;
                case RoleConstants.ROLE_JURY:
                    userRole = roleRepository.findByName(RoleConstants.ROLE_JURY);
                    break;
                default:
                    userRole = roleRepository.findByName(RoleConstants.ROLE_USER);
            }
        }

        user.setRole(userRole.get());
        userRepository.save(user);
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(signUpRequest.getEmail());
        simpleMailMessage.setSubject("User Credentials");
        simpleMailMessage.setText("You have successfully registered on the ERK service! Your login: "
                + signUpRequest.getUsername() + "\n Your password: " + signUpRequest.getPassword());

        this.emailSender.send(simpleMailMessage);
    }

}
