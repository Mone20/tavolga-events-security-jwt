package com.tavolgaevents.backend.repository;

import java.util.List;
import java.util.Optional;

import com.tavolgaevents.backend.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.tavolgaevents.backend.models.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
  Optional<User> findByUsername(String username);

  Boolean existsByUsername(String username);

  Boolean existsByEmail(String email);

  List<User> findByRole(Role role);
}
