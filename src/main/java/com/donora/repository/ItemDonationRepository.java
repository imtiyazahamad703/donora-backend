package com.donora.repository;

import com.donora.entity.ItemDonation;
import com.donora.entity.User;
import com.donora.enums.DonationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemDonationRepository extends JpaRepository<ItemDonation, Long> {

    List<ItemDonation> findByNgo(User ngo);

    // ✅ Add these two methods below for impact tracking
    int countByNgoAndStatus(User ngo, DonationStatus status);
    List<ItemDonation> findTop5ByNgoAndStatusOrderByCreatedAtDesc(User ngo, DonationStatus status);
    List<ItemDonation> findByNgoAndStatus(User ngo, DonationStatus status);
    List<ItemDonation> findByDonor(User donor);
    List<ItemDonation> findByDonorEmailOrderByCreatedAtDesc(String email);

}
