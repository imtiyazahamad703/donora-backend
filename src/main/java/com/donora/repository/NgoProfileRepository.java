package com.donora.repository;

import com.donora.entity.NgoProfile;
import com.donora.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NgoProfileRepository extends JpaRepository<NgoProfile, Long> {

    Optional<NgoProfile> findByUser(User user);

    boolean existsByUser(User user);
}
