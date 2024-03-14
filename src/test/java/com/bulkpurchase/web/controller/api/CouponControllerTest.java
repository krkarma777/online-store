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
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    private final Coupon coupon = new Coupon();;

    @BeforeEach
    public void setUp() {
        mockUser = new User();
        coupon.setCouponID(1L);
    }

    @Test
    @WithMockUser(username = "qweqwe", roles = "판매자")
    public void createCouponTest() throws Exception {
        // Given
        CouponCreateRequestDTO requestDTO = new CouponCreateRequestDTO();

        // When & Then
        mockMvc.perform(post("/api/coupon")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"message\":\"쿠폰이 정상적으로 생성되었습니다.\"}"));

        verify(couponService, times(1)).save(any(Coupon.class));
    }

    @Test
    @WithMockUser(username = "zxczxc", roles = "자영업자")
    public void updateCouponTest() throws Exception {
        // Given
        Long couponId = coupon.getCouponID();
        CouponUpdateRequestDTO couponUpdateRequestDTO = new CouponUpdateRequestDTO();

        when(userAuthValidator.getCurrentUser(any(Principal.class))).thenReturn(mockUser);
        when(couponService.findByIdAndUser(eq(couponId), eq(mockUser))).thenReturn(Optional.of(coupon));

        // When & Then
        mockMvc.perform(put("/api/coupon/{couponID}", couponId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(couponUpdateRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"message\":\"쿠폰이 정상적으로 수정되었습니다.\"}"));

        verify(couponService, times(1)).save(any(Coupon.class));
    }

    @Test
    @WithMockUser(username = "zxczxc", roles = "자영업자")
    public void deleteCouponTest() throws Exception {
        // Given
        Long couponId = coupon.getCouponID();

        when(userAuthValidator.getCurrentUser(any(Principal.class))).thenReturn(mockUser);
        when(couponService.findByIdAndUser(couponId, mockUser)).thenReturn(Optional.of(coupon));

        // When & Then
        mockMvc.perform(delete("/api/coupon/{couponID}", couponId))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"message\":\"쿠폰이 정상적으로 삭제되었습니다.\"}"));

        verify(couponService).delete(coupon);
    }

    @Test
    @WithMockUser(username = "testUser")
    public void getCouponTest_Success() throws Exception {
        // Given
        Long couponID = coupon.getCouponID();

        when(couponService.findById(eq(couponID))).thenReturn(Optional.of(coupon));

        // When & Then
        mockMvc.perform(get("/api/coupon/{couponID}", couponID))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(coupon)));
    }

    @Test
    @WithMockUser(username = "testUser")
    public void getCouponTest_NotFound() throws Exception {
        // Given
        Long couponId = 2L;

        when(couponService.findById(eq(couponId))).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/api/coupon/{couponID}", couponId))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"message\":\"쿠폰이 존재하지 않습니다.\"}"));
    }

    @Test
    @WithMockUser(username = "testUser")
    public void listCouponsTest() throws Exception {
        // Given
        List<Coupon> expectedCoupons = List.of(
                new Coupon(),
                new Coupon()
        );
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        when(couponService.findAll(sort)).thenReturn(expectedCoupons);

        // When & Then
        mockMvc.perform(get("/api/coupon/list"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedCoupons)));

        verify(couponService).findAll(sort);
    }
}