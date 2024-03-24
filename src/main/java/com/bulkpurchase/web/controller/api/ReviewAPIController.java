package com.bulkpurchase.web.controller.api;

import com.bulkpurchase.domain.dto.review.ReviewDetailDTO;
import com.bulkpurchase.domain.dto.review.ReviewUpdateRequestDTO;
import com.bulkpurchase.domain.dto.review.ReviewWriteRequestDTO;
import com.bulkpurchase.domain.entity.order.OrderDetail;
import com.bulkpurchase.domain.entity.product.Product;
import com.bulkpurchase.domain.entity.review.Review;
import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.enums.UserRole;
import com.bulkpurchase.domain.service.order.OrderDetailService;
import com.bulkpurchase.domain.service.product.ProductService;
import com.bulkpurchase.domain.service.review.ReviewService;
import com.bulkpurchase.domain.validator.user.UserAuthValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/review")
public class ReviewAPIController {

    private final ReviewService reviewService;
    private final ProductService productService;
    private final UserAuthValidator userAuthValidator;
    private final OrderDetailService orderDetailService;

    @PostMapping
    public ResponseEntity<?> reviewWrite(@RequestBody ReviewWriteRequestDTO reviewWriteRequestDTO, Principal principal) {
        User user = getUser(principal);

        Optional<Product> productOpt = productService.findById(reviewWriteRequestDTO.getProductID());
        if (productOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "존재하지 않는 상품입니다."));
        }

        Optional<OrderDetail> orderDetailOpt = orderDetailService.findByID(reviewWriteRequestDTO.getOrderDetailID());
        if (orderDetailOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "결제한 상품에 대한 리뷰만 가능합니다."));
        }

        Product product = productOpt.get();
        OrderDetail orderDetail = orderDetailOpt.get();

        Review review = new Review(product, user, reviewWriteRequestDTO.getContent(), reviewWriteRequestDTO.getRating(), orderDetail);
        reviewService.save(review);

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "리뷰가 성공적으로 작성되었습니다."));
    }

    @DeleteMapping("/{reviewID}")
    public ResponseEntity<?> delete(@PathVariable("reviewID") Long reviewID, Principal principal) {
        User user = getUser(principal);
        Optional<Review> reviewOpt = reviewService.findByReviewIDAndUser(reviewID, user);

        if (reviewOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "존재하지 않는 리뷰입니다."));
        }

        reviewService.delete(reviewOpt.get());
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("message", "리뷰가 삭제되었습니다."));
    }

    @PatchMapping("/{reviewID}")
    public ResponseEntity<?> update(@PathVariable("reviewID") Long reviewID, @RequestBody ReviewUpdateRequestDTO reviewUpdateRequestDTO, Principal principal) {
        User user = getUser(principal);
        Optional<Review> reviewOpt = reviewService.findByReviewIDAndUser(reviewID, user);

        if (reviewOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "존재하지 않는 리뷰입니다."));
        }

        Review review = reviewOpt.get();
        review.setRating(reviewUpdateRequestDTO.getRating());
        review.setContent(reviewUpdateRequestDTO.getContent());
        reviewService.save(review);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("message", "리뷰가 수정되었습니다."));
    }

    @GetMapping("/{reviewID}")
    public ResponseEntity<?>  reviewDetail(@PathVariable("reviewID") Long reviewID) {
        Optional<ReviewDetailDTO> reviewOpt = reviewService.reviewDetailsByReviewID(reviewID);
        if (reviewOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "존재하지 않는 리뷰입니다."));
        }
        ReviewDetailDTO review = reviewOpt.get();
        return ResponseEntity.ok(review);
    }

    @GetMapping
    public ResponseEntity<?> reviewList(Principal principal, @RequestParam(value = "page", defaultValue = "1") int page) {
        User user = getUser(principal);
        Sort sort = Sort.by(Sort.Direction.DESC, "creationDate");
        Pageable pageable = PageRequest.of(page - 1, 10, sort);
        Page<ReviewDetailDTO> reviewDetailDTOS = reviewService.findByUser(user, pageable);
        return ResponseEntity.ok(reviewDetailDTOS);
    }

    @GetMapping("/seller/{sellerID}")
    public ResponseEntity<?> findReviewsBySeller(@PathVariable("sellerID") Long sellerID,
                                          @RequestParam(value = "page", defaultValue = "1") Integer page) {
        User user = userAuthValidator.getCurrentUserByUserID(sellerID);

        if (user.getRole() != UserRole.ROLE_판매자) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "잘못된 요청입니다."));
        }

        Sort sort = Sort.by(Sort.Direction.DESC, "reviewID");
        Pageable pageable = PageRequest.of(page - 1, 10, sort);
        Page<ReviewDetailDTO> reviewDetailDTOS = reviewService.findByUser(user, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(reviewDetailDTOS);
    }

    private User getUser(Principal principal) {
        return userAuthValidator.getCurrentUser(principal);
    }
}
