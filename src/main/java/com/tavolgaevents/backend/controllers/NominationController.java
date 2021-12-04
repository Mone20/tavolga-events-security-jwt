package com.tavolgaevents.backend.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import com.tavolgaevents.backend.models.Nomination;
import com.tavolgaevents.backend.models.Views;
import com.tavolgaevents.backend.payload.request.NominationRequest;
import com.tavolgaevents.backend.repository.NominationRepository;
import com.tavolgaevents.backend.services.NominationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/nominations")
public class NominationController {
    
    @Autowired
    NominationService nominationService;

    @Autowired
    NominationRepository nominationRepository;

    @JsonView(Views.Public.class)
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Nomination>> getAllNominations() {
        List<Nomination> nominations = (List<Nomination>) nominationRepository.findAll();
        return nominations.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(nominations);
    }
    @JsonView(Views.Public.class)
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('ASSESSOR') or hasRole('JURY') ")
    public ResponseEntity<List<Nomination>> getNominationsByUserId(@PathVariable String userId) {
        List<Nomination> nominations = nominationService.getNominationByUserId(Long.parseLong(userId));
        return nominations.isEmpty()? ResponseEntity.noContent().build() : ResponseEntity.ok(nominations);
    }
    @JsonView(Views.Public.class)
    @GetMapping("/assessor/{userId}")
    @PreAuthorize("hasRole('ASSESSOR') or hasRole('ADMIN')  or hasRole('JURY')")
    public ResponseEntity<List<Nomination>> getRateNominationsByUserId(@PathVariable String userId) {
        List<Nomination> nominations = nominationService.getNominationByAssessorId(Long.parseLong(userId));
        return nominations.isEmpty()? ResponseEntity.noContent().build() : ResponseEntity.ok(nominations);
    }

    @JsonView(Views.Public.class)
    @GetMapping("/assessor/{userId}/{contestId}")
    @PreAuthorize("hasRole('ASSESSOR') or hasRole('ADMIN')  or hasRole('JURY')")
    public ResponseEntity<List<Nomination>> getRateNominationsByUserIdAndContestId(@PathVariable String userId, @PathVariable String contestId) {
        List<Nomination> nominations = nominationService.getNominationByAssessorId(Long.parseLong(userId));
        nominations = nominationService.filterByContestId(Long.parseLong(contestId), nominations);
        return nominations.isEmpty()? ResponseEntity.noContent().build() : ResponseEntity.ok(nominations);
    }

    @JsonView(Views.Public.class)
    @GetMapping("/contest/{userId}/{contestId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<Nomination>> getNominationsByUserIdAndContestId(@PathVariable String userId, @PathVariable String contestId) {
        List<Nomination> nominations = nominationService.getNominationByUserId(Long.parseLong(userId));
        nominations = nominationService.filterByContestId(Long.parseLong(contestId), nominations);
        return nominations.isEmpty()? ResponseEntity.noContent().build() : ResponseEntity.ok(nominations);
    }


    @JsonView(Views.Public.class)
    @PostMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Nomination> createNomination(@RequestBody NominationRequest request) {
        return ResponseEntity.ok(nominationService.create(request));
    }

    @JsonView(Views.Public.class)
    @PutMapping("/{nominationId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Nomination> updateNomination(@RequestBody NominationRequest request, @PathVariable String nominationId) {
        return ResponseEntity.ok(nominationService.update(Long.parseLong(nominationId), request));
    }

    @JsonView(Views.Public.class)
    @DeleteMapping("/{nominationId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> deleteNomination(@PathVariable String nominationId) {
        nominationService.delete(Long.parseLong(nominationId));
        return ResponseEntity.ok().build();
    }
}
