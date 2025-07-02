package com.donora.repository;

import com.donora.entity.BusinessProfile;
import com.donora.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BusinessProfileRepository extends JpaRepository<BusinessProfile, Long> {
    Optional<BusinessProfile> findByUser(User user);
}
