package com.donora.repository;

import com.donora.entity.ItemRequest;
import com.donora.entity.User;
import com.donora.enums.DonationStatus;
import com.donora.enums.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {

    List<ItemRequest> findByNgo(User ngo);

    List<ItemRequest> findByStatus(RequestStatus status); // for future use, like public browsing by users
    List<ItemRequest> findByNgoAndIsEmergencyTrue(User ngo);
    List<ItemRequest> findByIsEmergencyTrue();

}
