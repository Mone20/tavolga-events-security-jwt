package com.tavolgaevents.backend.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import com.tavolgaevents.backend.models.Criterion;
import com.tavolgaevents.backend.models.Views;
import com.tavolgaevents.backend.payload.request.CriterionRequest;
import com.tavolgaevents.backend.repository.CriteriaRepository;
import com.tavolgaevents.backend.repository.NominationRepository;
import com.tavolgaevents.backend.services.CriterionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/criterion")
public class CriterionController {

    @Autowired
    CriterionService criterionService;

    @Autowired
    NominationRepository nominationRepository;

    @Autowired
    CriteriaRepository criterionRepository;

    @JsonView(Views.Public.class)
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Criterion>> getAllCriteria() {
        List<Criterion> criterion = (List<Criterion>) criterionRepository.findAll();
        return criterion.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(criterion);
    }

    @JsonView(Views.Public.class)
    @GetMapping("/{nominationId}")
    @PreAuthorize("hasRole('ASSESSOR') or hasRole('ADMIN')  or hasRole('JURY') or hasRole('USER')")
    public ResponseEntity<List<Criterion>> getCriteriaByNominationId(@PathVariable String nominationId) {
        List<Criterion> criterion = nominationRepository.findById(Long.valueOf(nominationId)).get().getCriterionList();
        return criterion.isEmpty()? ResponseEntity.noContent().build() : ResponseEntity.ok(criterion);
    }


    @JsonView(Views.Public.class)
    @PostMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Criterion> createCriterion(@RequestBody CriterionRequest request) {
        return ResponseEntity.ok(criterionService.create(request));
    }

    @JsonView(Views.Public.class)
    @PutMapping("/{criterionId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Criterion> updateCriterion(@RequestBody CriterionRequest request, @PathVariable String criterionId) {
        return ResponseEntity.ok(criterionService.update(Long.parseLong(criterionId), request));
    }

    @JsonView(Views.Public.class)
    @DeleteMapping("/{criterionId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> deleteCriterion(@PathVariable String criterionId) {
        criterionService.delete(Long.parseLong(criterionId));
        return ResponseEntity.ok().build();
    }
}
