package com.bulkpurchase.domain.service.review;

import com.bulkpurchase.domain.repository.review.ReviewLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewLikeService {

    private final ReviewLikeRepository reviewLikeRepository;
}
