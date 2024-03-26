package com.bulkpurchase.web.controller.api;

import com.bulkpurchase.domain.dto.coupon.CouponApplicableProductRequestDTO;
import com.bulkpurchase.domain.entity.coupon.Coupon;
import com.bulkpurchase.domain.entity.product.Product;
import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.service.coupon.CouponApplicableProductService;
import com.bulkpurchase.domain.service.coupon.CouponService;
import com.bulkpurchase.domain.service.product.ProductService;
import com.bulkpurchase.domain.validator.user.UserAuthValidator;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class CouponApplicableProductAPIControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CouponApplicableProductService couponApplicableProductService;

    @MockBean
    private CouponService couponService;

    @MockBean
    private UserAuthValidator userAuthValidator;

    @MockBean
    private ProductService productService;


    @Test
    @WithMockUser
    public void selectProductForCoupon_ShouldProcessRequest_WhenGivenValidData() throws Exception {
        CouponApplicableProductRequestDTO requestDTO = new CouponApplicableProductRequestDTO();
        requestDTO.setCouponID(1L);
        requestDTO.setProductIDs(List.of(1L, 2L, 3L));

        User user = new User();

        Product product1 = new Product(1L, user);
        Product product2 = new Product(2L, user);
        Product product3 = new Product(3L, user);

        Coupon coupon = new Coupon();
        coupon.setCouponID(1L);
        coupon.setCreatedBy(user);

        given(couponService.findById(1L)).willReturn(Optional.of(coupon));
        given(userAuthValidator.getCurrentUser(any(Principal.class))).willReturn(user);
        given(productService.findById(1L)).willReturn(Optional.of(product1));
        given(productService.findById(2L)).willReturn(Optional.of(product2));
        given(productService.findById(3L)).willReturn(Optional.of(product3));

        String jsonRequest = objectMapper.writeValueAsString(requestDTO);

        mockMvc.perform(post("/api/coupon-applicable-product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("쿠폰에 상품(들)이 정상적으로 적용되었습니다."));
    }
}
