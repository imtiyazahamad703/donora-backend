package com.donora.repository;

import com.donora.entity.IndividualProfile;
import com.donora.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IndividualProfileRepository extends JpaRepository<IndividualProfile, Long> {
    Optional<IndividualProfile> findByUser(User user);
}
