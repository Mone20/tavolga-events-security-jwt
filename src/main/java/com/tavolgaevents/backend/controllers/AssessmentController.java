package com.tavolgaevents.backend.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import com.tavolgaevents.backend.models.*;
import com.tavolgaevents.backend.payload.request.AssessmentRequest;
import com.tavolgaevents.backend.repository.AssessmentRepository;
import com.tavolgaevents.backend.repository.ContestRepository;
import com.tavolgaevents.backend.repository.CriteriaRepository;
import com.tavolgaevents.backend.repository.NominationRepository;
import com.tavolgaevents.backend.services.AssessmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/assessment")
public class AssessmentController {

    @Autowired
    private AssessmentRepository assessmentRepository;

    @Autowired
    private CriteriaRepository criteriaRepository;

    @Autowired
    private NominationRepository nominationRepository;

    @Autowired
    private ContestRepository contestRepository;

    @Autowired
    private AssessmentService assessmentService;

    @JsonView(Views.Public.class)
    @GetMapping("/{contestId}/{nominationId}/{criterionId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER') or hasRole('JURY') or hasRole('ASSESSOR')")
    public ResponseEntity<List<Assessment>> getAssessmentsByContestNominationCriterionIds(@PathVariable String contestId,
                                                                                            @PathVariable String nominationId,
                                                                                             @PathVariable String criterionId) {
        Contest contest = contestRepository.findById(Long.valueOf(contestId)).get();
        Nomination nomination = nominationRepository.findById(Long.valueOf(nominationId)).get();
        Criterion criterion = criteriaRepository.findById(Long.valueOf(criterionId)).get();
        List<Assessment> assessments = assessmentRepository.findByContestAndNominationAndCriterion(contest, nomination, criterion);
        return assessments.isEmpty()? ResponseEntity.noContent().build() : ResponseEntity.ok(assessments);
    }

    @JsonView(Views.Public.class)
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Assessment>> getAllAssessments() {
        List<Assessment> assessments = (List<Assessment>) assessmentRepository.findAll();
        return assessments.isEmpty()? ResponseEntity.noContent().build() : ResponseEntity.ok(assessments);
    }

    @JsonView(Views.Public.class)
    @PostMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Assessment> createAssessment(@RequestBody AssessmentRequest request) {
        return ResponseEntity.ok(assessmentService.create(request));
    }

    @JsonView(Views.Public.class)
    @PutMapping("/{assessmentId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Assessment> updateAssessment(@RequestBody AssessmentRequest request, @PathVariable String assessmentId) {
        return ResponseEntity.ok(assessmentService.update(Long.parseLong(assessmentId), request));
    }

    @JsonView(Views.Public.class)
    @DeleteMapping("/{assessmentId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> deleteAssessment(@PathVariable String assessmentId) {
        assessmentService.delete(Long.parseLong(assessmentId));
        return ResponseEntity.ok().build();
    }
}
