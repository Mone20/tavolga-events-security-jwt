package com.tavolgaevents.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.tavolgaevents.backend.models.Role;

@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {
  Optional<Role> findByName(String name);

  Optional<Role> findById(Integer id);

    boolean existsByName(String role);
}
