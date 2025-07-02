package com.donora.repository;

import com.donora.entity.ItemDonation;
import com.donora.entity.User;
import com.donora.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    List<User> findByRoleIn(List<Role> roles);


}
