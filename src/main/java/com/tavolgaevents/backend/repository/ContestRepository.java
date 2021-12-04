package com.tavolgaevents.backend.repository;

import com.tavolgaevents.backend.models.Contest;
import com.tavolgaevents.backend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface ContestRepository  extends CrudRepository<Contest, Long> {

}
