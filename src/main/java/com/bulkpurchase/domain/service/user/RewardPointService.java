package com.bulkpurchase.domain.service.user;

import com.bulkpurchase.domain.entity.user.RewardPoint;
import com.bulkpurchase.domain.repository.user.RewardPointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RewardPointService {

    private final RewardPointRepository rewardPointRepository;

    public List<RewardPoint> findAvailableByUserUserID(Long userId) {
        return rewardPointRepository.findAvailableByUserUserID(userId);
    }

    public void save(RewardPoint point) {
        rewardPointRepository.save(point);
    }

}
