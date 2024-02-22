package com.bulkpurchase.web.service;

import com.bulkpurchase.domain.entity.user.RewardPoint;
import com.bulkpurchase.domain.enums.RewardPointStatus;
import com.bulkpurchase.domain.service.user.RewardPointService;
import com.bulkpurchase.domain.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UseRewardPointService {

    private final UserService userService;
    private final RewardPointService rewardPointService;

    public void useRewardPoints(Long userId, Integer amountToUse) {
        // 사용자의 사용 가능한 적립금 조회
        List<RewardPoint> availablePoints = rewardPointService.findAvailableByUserUserID(userId);

        Integer remainingAmount = amountToUse;

        for (RewardPoint point : availablePoints) {
            if (remainingAmount <= 0) break;

            if (point.getAmount() <= remainingAmount) {
                // 적립금 전액 사용 처리
                point.setStatus(RewardPointStatus.USED);
                point.setUsedDate(LocalDateTime.now());
                remainingAmount -= point.getAmount();
            } else {
                // 적립금의 일부만 사용하는 경우, 새로운 적립금 항목 생성 (분할 사용)
                point.setAmount(point.getAmount() - remainingAmount);
                createNewUsedRewardPoint(userId, remainingAmount);
                remainingAmount = 0;
            }

            rewardPointService.save(point);
        }

        if (remainingAmount > 0) {
            throw new IllegalArgumentException("Not enough reward points");
        }
    }

    private void createNewUsedRewardPoint(Long userId, Integer usedAmount) {
        RewardPoint usedPoint = new RewardPoint();
        usedPoint.setUser(userService.findByUserid(userId).orElseThrow());
        usedPoint.setAmount(usedAmount);
        usedPoint.setStatus(RewardPointStatus.USED);
        usedPoint.setEarnedDate(LocalDateTime.now());
        usedPoint.setUsedDate(LocalDateTime.now());
        rewardPointService.save(usedPoint);
    }
}
