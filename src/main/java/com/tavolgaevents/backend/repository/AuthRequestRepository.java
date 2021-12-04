package com.tavolgaevents.backend.repository;

import com.tavolgaevents.backend.models.AuthRequest;
import org.springframework.data.repository.CrudRepository;

public interface AuthRequestRepository extends CrudRepository<AuthRequest, Long> {
}
