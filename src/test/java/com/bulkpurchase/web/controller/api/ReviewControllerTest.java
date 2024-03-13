package com.bulkpurchase.web.controller.api;

import com.bulkpurchase.domain.dto.review.ReviewResponseDTO;
import com.bulkpurchase.domain.dto.review.ReviewUpdateRequestDTO;
import com.bulkpurchase.domain.dto.review.ReviewWriteRequestDTO;
import com.bulkpurchase.domain.entity.order.Order;
import com.bulkpurchase.domain.entity.order.OrderDetail;
import com.bulkpurchase.domain.entity.product.Product;
import com.bulkpurchase.domain.entity.review.Review;
import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.service.order.OrderDetailService;
import com.bulkpurchase.domain.service.product.ProductService;
import com.bulkpurchase.domain.service.review.ReviewService;
import com.bulkpurchase.domain.validator.user.UserAuthValidator;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReviewService reviewService;

    @MockBean
    private ProductService productService;

    @MockBean
    private OrderDetailService orderDetailService;

    @MockBean
    private UserAuthValidator userAuthValidator;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    public void reviewWrite_WhenProductAndOrderDetailExist_ShouldReturnCreated() throws Exception {
        // Given
        ReviewWriteRequestDTO requestDTO = new ReviewWriteRequestDTO("Great product", 5, 1L, 1L);
        User user = new User();
        Product product = new Product();
        OrderDetail orderDetail = new OrderDetail();

        given(userAuthValidator.getCurrentUser(any())).willReturn(user);
        given(productService.findById(requestDTO.getProductID())).willReturn(Optional.of(product));
        given(orderDetailService.findByID(requestDTO.getOrderDetailID())).willReturn(Optional.of(orderDetail));

        // When & Then
        mockMvc.perform(post("/api/review")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"message\":\"리뷰가 성공적으로 작성되었습니다.\"}"));
    }

    @Test
    @WithMockUser
    public void deleteReview_WhenReviewExists_ShouldReturnOk() throws Exception {
        // Given
        Long reviewID = 1L;
        User user = new User();
        Review review = new Review();
        given(userAuthValidator.getCurrentUser(any())).willReturn(user);
        given(reviewService.findByReviewIDAndUser(reviewID, user)).willReturn(Optional.of(review));

        // When & Then
        mockMvc.perform(delete("/api/review/{reviewID}", reviewID))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"message\":\"리뷰가 삭제되었습니다.\"}"));
    }

    @Test
    @WithMockUser
    public void deleteReview_WhenReviewDoesNotExist_ShouldReturnNotFound() throws Exception {
        // Given
        Long reviewID = 1L;
        User user = new User();
        given(userAuthValidator.getCurrentUser(any())).willReturn(user);
        given(reviewService.findByReviewIDAndUser(reviewID, user)).willReturn(Optional.empty());

        // When & Then
        mockMvc.perform(delete("/api/review/{reviewID}", reviewID))
                .andExpect(status().isNotFound())
                .andExpect(content().json("{\"message\":\"존재하지 않는 리뷰입니다.\"}"));
    }

    @Test
    @WithMockUser
    public void update_WhenReviewExists_ShouldReturnOkAndMessage() throws Exception {
        // Given
        Long reviewId = 1L;
        ReviewUpdateRequestDTO requestDTO = new ReviewUpdateRequestDTO("Updated content", 5); // Adjust fields as necessary
        Review review = new Review();
        review.setReviewID(reviewId);

        User user = new User();
        given(userAuthValidator.getCurrentUser(any())).willReturn(user);
        given(reviewService.findByReviewIDAndUser(eq(reviewId), eq(user))).willReturn(Optional.of(review));

        ObjectMapper objectMapper = new ObjectMapper();

        // When & Then
        mockMvc.perform(patch("/api/review/{reviewID}", reviewId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("리뷰가 수정되었습니다."));
    }

    @Test
    @WithMockUser
    public void update_ReviewDoesNotExist_ShouldReturnNotFound() throws Exception {
        // Given
        given(reviewService.findByReviewIDAndUser(any(Long.class), any(User.class))).willReturn(Optional.empty());

        ReviewUpdateRequestDTO requestDTO = new ReviewUpdateRequestDTO();
        requestDTO.setRating(5);
        requestDTO.setContent("Non-existent review");

        // When & Then
        mockMvc.perform(patch("/api/review/{reviewID}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDTO)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void update_UserNotAuthenticated_ShouldReturnUnauthorized() throws Exception {
        // Given
        ReviewUpdateRequestDTO requestDTO = new ReviewUpdateRequestDTO();
        requestDTO.setRating(5);
        requestDTO.setContent("Unauthorized update attempt");

        // When & Then
        mockMvc.perform(patch("/api/review/{reviewID}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDTO)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void reviewDetail_WhenReviewExists_ShouldReturnReview() throws Exception {
        // Given
        long reviewId = 1L;
        Review review = createTestReview(reviewId);
        ReviewResponseDTO expectedResponseDTO = new ReviewResponseDTO(review);

        given(reviewService.findById(reviewId)).willReturn(Optional.of(review));

        // When & Then
        mockMvc.perform(get("/api/review/{reviewID}", reviewId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    private Review createTestReview(long reviewId) {
        Product product = new Product();
        product.setImageUrls(List.of("test"));

        Order order = new Order();
        order.setOrderID(1L);

        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrder(order);

        Review review = new Review(product, new User(), "testReview", 5, orderDetail);
        review.setReviewID(reviewId);

        return review;
    }

    @Test
    @WithMockUser
    void reviewDetail_WhenReviewDoesNotExist_ShouldReturnBadRequest() throws Exception {
        // Given
        given(reviewService.findById(anyLong())).willReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/api/review/{reviewID}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().json("{\"message\":\"존재하지 않는 리뷰입니다.\"}"));
    }
}