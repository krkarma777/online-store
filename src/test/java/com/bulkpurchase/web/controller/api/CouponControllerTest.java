package com.bulkpurchase.web.controller.api;

import com.bulkpurchase.domain.dto.coupon.CouponCreateRequestDTO;
import com.bulkpurchase.domain.dto.coupon.CouponUpdateRequestDTO;
import com.bulkpurchase.domain.entity.coupon.Coupon;
import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.service.coupon.CouponService;
import com.bulkpurchase.domain.validator.user.UserAuthValidator;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.security.Principal;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class CouponControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CouponService couponService;

    @MockBean
    private UserAuthValidator userAuthValidator;

    private User mockUser;

    private Coupon coupon;

    @BeforeEach
    public void setUp() {
        mockUser = new User();
        Coupon couponOrigin = new Coupon();
        couponOrigin.setCouponID(1L);
        coupon = couponOrigin;
    }

    @Test
    @WithMockUser(username = "qweqwe",roles = "판매자")
    public void createCouponTest() throws Exception {
        // Given
        CouponCreateRequestDTO requestDTO = new CouponCreateRequestDTO();

        // When & Then
        mockMvc.perform(post("/api/coupon")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"message\":\"쿠폰이 정상적으로 생성되었습니다.\"}"));
    }

    @Test
    @WithMockUser(username = "zxczxc",roles = "자영업자")
    public void updateCouponTest() throws Exception {
        Long couponId = coupon.getCouponID();

        when(userAuthValidator.getCurrentUser(any(Principal.class))).thenReturn(mockUser);
        when(couponService.findByIdAndUser(eq(couponId), eq(mockUser))).thenReturn(Optional.of(coupon));

        CouponUpdateRequestDTO couponUpdateRequestDTO = new CouponUpdateRequestDTO();

        mockMvc.perform(put("/api/coupon/{couponID}", couponId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(couponUpdateRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"message\":\"쿠폰이 정상적으로 수정되었습니다.\"}"));

        verify(couponService, times(1)).save(any(Coupon.class));
    }
}