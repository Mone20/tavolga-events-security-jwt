package com.tavolgaevents.backend.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import com.tavolgaevents.backend.models.Contest;
import com.tavolgaevents.backend.models.Views;
import com.tavolgaevents.backend.payload.request.ContestRequest;
import com.tavolgaevents.backend.repository.ContestRepository;
import com.tavolgaevents.backend.services.ContestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/contest")
public class ContestController {

    @Autowired
    private ContestRepository contestRepository;
    @Autowired
    private ContestService contestService;


    @JsonView(Views.Public.class)
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Contest>> getAllContests() {
        List<Contest> contests = (List<Contest>) contestRepository.findAll();
        return contests.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(contests);
    }

    @JsonView(Views.Public.class)
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER') or hasRole('JURY') or hasRole('ASSESSOR')")
    public ResponseEntity<List<Contest>> getContestsByUserId(@PathVariable String userId) {
        List<Contest> contests = contestService.getContestByUserId(Long.parseLong(userId), false);
        return contests.isEmpty()? ResponseEntity.noContent().build() : ResponseEntity.ok(contests);
    }

    @JsonView(Views.Public.class)
    @GetMapping("/assessor/{userId}")
    @PreAuthorize("hasRole('ADMIN') or  hasRole('JURY') or hasRole('ASSESSOR')")
    public ResponseEntity<List<Contest>> getContestsByAssessorId(@PathVariable String userId) {
        List<Contest> contests = contestService.getContestByUserId(Long.parseLong(userId), true);
        return contests.isEmpty()? ResponseEntity.noContent().build() : ResponseEntity.ok(contests);
    }

    @JsonView(Views.Public.class)
    @PostMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Contest> createContest(@RequestBody ContestRequest request) {
        return ResponseEntity.ok(contestService.create(request));
    }

    @JsonView(Views.Public.class)
    @PutMapping("/{contestId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Contest> updateContest(@RequestBody ContestRequest request, @PathVariable String contestId) {
        return ResponseEntity.ok(contestService.update(Long.parseLong(contestId), request));
    }

    @JsonView(Views.Public.class)
    @DeleteMapping("/{contestId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> deleteContest(@PathVariable String contestId) {
        contestService.delete(Long.parseLong(contestId));
        return ResponseEntity.ok().build();
    }

}
