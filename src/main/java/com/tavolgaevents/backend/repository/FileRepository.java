package com.tavolgaevents.backend.repository;

import com.tavolgaevents.backend.models.File;
import com.tavolgaevents.backend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FileRepository extends CrudRepository<File, Long> {
    List<File> findByUser(User user);
}
