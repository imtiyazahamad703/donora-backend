package com.donora.repository;

import com.donora.entity.FoodDonation;
import com.donora.entity.User;
import com.donora.enums.DonationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FoodDonationRepository extends JpaRepository<FoodDonation, Long> {

    List<FoodDonation> findByNgo(User ngo);
    List<FoodDonation> findByDonor(User donor);
    // ✅ Add these two methods for impact tracking
    int countByNgoAndStatus(User ngo, DonationStatus status);
    List<FoodDonation> findTop5ByNgoAndStatusOrderByCreatedAtDesc(User ngo, DonationStatus status);
    List<FoodDonation> findByNgoAndStatus(User ngo, DonationStatus status);

}
