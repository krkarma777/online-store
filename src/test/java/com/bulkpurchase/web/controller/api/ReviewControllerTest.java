package com.bulkpurchase.web.controller.api;

import com.bulkpurchase.domain.dto.review.ReviewWriteRequestDTO;
import com.bulkpurchase.domain.entity.order.OrderDetail;
import com.bulkpurchase.domain.entity.product.Product;
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

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
                .andExpect(status().isCreated());
    }

}