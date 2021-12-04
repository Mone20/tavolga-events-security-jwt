package com.tavolgaevents.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.tavolgaevents.backend.models.RefreshToken;
import com.tavolgaevents.backend.models.User;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long> {
  Optional<RefreshToken> findByToken(String token);

  @Modifying
  int deleteByUser(User user);
}
