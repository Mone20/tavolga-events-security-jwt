package com.tavolgaevents.backend.services.impl;

import com.tavolgaevents.backend.models.Assessment;
import com.tavolgaevents.backend.payload.request.AssessmentRequest;
import com.tavolgaevents.backend.repository.AssessmentRepository;
import com.tavolgaevents.backend.repository.ContestRepository;
import com.tavolgaevents.backend.repository.CriteriaRepository;
import com.tavolgaevents.backend.repository.NominationRepository;
import com.tavolgaevents.backend.services.AssessmentService;
import com.tavolgaevents.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.sql.Date;
import java.time.LocalDateTime;

@Service
public class AssessmentServiceImpl implements AssessmentService {
    @Autowired
    private UserService userService;

    @Autowired
    private CriteriaRepository criteriaRepository;

    @Autowired
    private NominationRepository nominationRepository;

    @Autowired
    private ContestRepository contestRepository;

    @Autowired
    private AssessmentRepository assessmentRepository;

    @Override
    public Assessment create(AssessmentRequest request) {
        Assessment assessment = new Assessment();
        return update(assessment, request);
    }

    private Assessment update(Assessment assessment, AssessmentRequest request) {
        assessment.setApprovalDateTime(request.approvalDateTime == null ? Date.valueOf(LocalDateTime.now().toLocalDate()) : request.approvalDateTime);
        assessment.setAssessor(userService.getUserById(Long.valueOf(request.assessorId)));
        assessment.setParticipant(userService.getUserById(Long.valueOf(request.userId)));
        assessment.setAssessment(request.assessment);
        assessment.setComment(request.comment);
        assessment.setCriterion(criteriaRepository.findById(Long.valueOf(request.criterionId)).get());
        assessment.setNomination(nominationRepository.findById(Long.valueOf(request.nominationId)).get());
        assessment.setContest(contestRepository.findById(Long.valueOf(request.contestId)).get());
        assessment = assessmentRepository.save(assessment);
        return assessment;
    }

    @Override
    public Assessment update(Long id, AssessmentRequest request) {
        Assessment assessment = assessmentRepository.findById(id).get();
        return update(assessment, request);
    }

    @Override
    public void delete(Long id) {
        assessmentRepository.deleteById(id);
    }
}