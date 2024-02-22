package com.bulkpurchase.domain.repository.user;

import com.bulkpurchase.domain.entity.user.RewardPoint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RewardPointRepository extends JpaRepository<RewardPoint, Long> {
    List<RewardPoint> findAvailableByUserUserID(Long userId);
}


