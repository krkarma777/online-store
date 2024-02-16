package com.bulkpurchase.domain.service.review;

import com.bulkpurchase.domain.repository.review.ReviewDislikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewDislikeService {

    private final ReviewDislikeRepository reviewDislikeRepository;
}
