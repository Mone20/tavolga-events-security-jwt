package com.tavolgaevents.backend.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import com.tavolgaevents.backend.models.*;
import com.tavolgaevents.backend.payload.request.SignupRequest;
import com.tavolgaevents.backend.repository.AuthRequestRepository;
import com.tavolgaevents.backend.repository.RoleRepository;
import com.tavolgaevents.backend.services.impl.AuthServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/api/auth/requests")
public class AuthRequestsController {

    @Autowired
    AuthServiceImpl authService;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    AuthRequestRepository authRequestRepository;

    @JsonView(Views.Public.class)
    @PostMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AuthRequest>> getAllRequests() {
        List<AuthRequest> authRequests = (List<AuthRequest>) authRequestRepository.findAll();
        return authRequests.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(authRequests);
    }

    @JsonView(Views.Public.class)
    @GetMapping("/access/{requestId}/{roleId}")
    @PreAuthorize("hasRole('ADMIN') ")
    public ResponseEntity<?> access(@PathVariable String requestId, @PathVariable String roleId) {
        AuthRequest authRequest = authRequestRepository.findById(Long.valueOf(requestId)).get();
        SignupRequest signupRequest = authRequest.convertToSignupRequest();
        Role role = roleRepository.findById(Integer.valueOf(roleId)).get();
        signupRequest.setRole(role.getName());
        authService.register(signupRequest);
        authRequestRepository.delete(authRequest);
        return ResponseEntity.ok().build();
    }

    @JsonView(Views.Public.class)
    @PostMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AuthRequest>> deleteRequest() {
        List<AuthRequest> authRequests = (List<AuthRequest>) authRequestRepository.findAll();
        return authRequests.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(authRequests);
    }

}
