package com.tavolgaevents.backend.services.impl;

import com.tavolgaevents.backend.models.Criterion;
import com.tavolgaevents.backend.payload.request.CriterionRequest;
import com.tavolgaevents.backend.repository.CriteriaRepository;
import com.tavolgaevents.backend.repository.NominationRepository;
import com.tavolgaevents.backend.services.CriterionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class CriterionServiceImpl implements CriterionService {

    @Autowired
    private CriteriaRepository criterionRepository;

    @Autowired
    private NominationRepository nominationRepository;

    @Override
    public Criterion create(CriterionRequest request) {
        Criterion criterion = new Criterion();
        return update(criterion, request);
    }

    private Criterion update(Criterion criterion, CriterionRequest request) {
        criterion.setDescription(request.description);
        criterion.setName(request.name);
        criterion.setMaxAssessment(request.maxAssessment);
        if(!StringUtils.isEmpty(request.nominationId))
            criterion.getNominations().add(nominationRepository.findById(Long.valueOf(request.nominationId)).get());

        criterion = criterionRepository.save(criterion);
        return criterion;
    }

    @Override
    public Criterion update(Long id, CriterionRequest request) {
        Criterion criterion = criterionRepository.findById(id).get();
        return update(criterion, request);
    }

    @Override
    public void delete(Long id) {
        criterionRepository.deleteById(id);
    }

}
