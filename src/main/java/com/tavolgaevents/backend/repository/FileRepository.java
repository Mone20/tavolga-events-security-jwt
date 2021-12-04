package com.tavolgaevents.backend.repository;

import com.tavolgaevents.backend.models.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface FileRepository extends CrudRepository<File, Long> {
}
