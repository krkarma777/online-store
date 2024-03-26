package com.bulkpurchase.web.controller.api;

import com.bulkpurchase.domain.dto.coupon.CouponCreateRequestDTO;
import com.bulkpurchase.domain.dto.coupon.CouponResponseDTO;
import com.bulkpurchase.domain.dto.coupon.CouponUpdateRequestDTO;
import com.bulkpurchase.domain.entity.coupon.Coupon;
import com.bulkpurchase.domain.entity.coupon.CouponApplicableProduct;
import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.enums.UserRole;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class CouponAPIControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CouponService couponService;

    @MockBean
    private UserAuthValidator userAuthValidator;

    private User mockUser;

    private final Coupon coupon1 = new Coupon();;
    private final Coupon coupon2 = new Coupon();;

    @BeforeEach
    public void setUp() {
        mockUser = new User();
        mockUser.setRole(UserRole.ROLE_판매자);

        coupon1.setCouponID(1L);
        coupon1.setCode(UUID.randomUUID().toString());
        coupon1.setCreatedBy(mockUser);
        coupon1.setIsActive(true);
        coupon1.setApplicableProducts(Set.of(new CouponApplicableProduct()));

        coupon2.setCouponID(2L);
        coupon2.setCode(UUID.randomUUID().toString());
        coupon2.setCreatedBy(mockUser);
        coupon2.setIsActive(true);
        coupon2.setApplicableProducts(Set.of(new CouponApplicableProduct()));

        List<Coupon> expectedCoupons = List.of(coupon1, coupon2);
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        when(couponService.findAll(sort)).thenReturn(expectedCoupons);
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
        Long couponId = coupon1.getCouponID();
        CouponUpdateRequestDTO couponUpdateRequestDTO = new CouponUpdateRequestDTO();

        when(userAuthValidator.getCurrentUser(any(Principal.class))).thenReturn(mockUser);
        when(couponService.findByIdAndUser(eq(couponId), eq(mockUser))).thenReturn(Optional.of(coupon1));

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
        Long couponId = coupon1.getCouponID();

        when(userAuthValidator.getCurrentUser(any(Principal.class))).thenReturn(mockUser);
        when(couponService.findByIdAndUser(couponId, mockUser)).thenReturn(Optional.of(coupon1));

        // When & Then
        mockMvc.perform(delete("/api/coupon/{couponID}", couponId))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"message\":\"쿠폰이 정상적으로 삭제되었습니다.\"}"));

        verify(couponService).delete(coupon1);
    }

    @Test
    @WithMockUser(username = "testUser")
    public void getCouponTest_Success() throws Exception {
        // Given
        Long couponID = coupon1.getCouponID();

        when(couponService.findById(eq(couponID))).thenReturn(Optional.of(coupon1));
        CouponResponseDTO couponResponseDTO = new CouponResponseDTO(coupon1);

        // When & Then
        mockMvc.perform(get("/api/coupon/{couponID}", couponID))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(couponResponseDTO)));
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
                coupon1,
                coupon2
        );
        Sort sort = Sort.by(Sort.Direction.DESC, "couponID");
        when(couponService.findAll(sort)).thenReturn(expectedCoupons);

        List<CouponResponseDTO> dtoList = expectedCoupons.stream()
                .map(CouponResponseDTO::new)
                .toList();

        // When & Then
        mockMvc.perform(get("/api/coupon/list"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(dtoList)));

        verify(couponService).findAll(sort);
    }

    @Test
    @WithMockUser(username = "validSeller", roles = "판매자")
    public void findListBySeller_Success() throws Exception {
        // Given
        Long sellerId = 1L;
        mockUser.setRole(UserRole.ROLE_판매자);
        when(userAuthValidator.getCurrentUserByUserID(eq(sellerId))).thenReturn(mockUser);
        Page<Coupon> mockPage = new PageImpl<>(List.of(new Coupon(), new Coupon()));
        when(couponService.findByCreatedBy(any(User.class), any(Pageable.class))).thenReturn(mockPage);

        // When & Then
        mockMvc.perform(get("/api/coupon/seller/{sellerID}", sellerId)
                        .param("page", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPages").value(mockPage.getTotalPages()))
                .andExpect(jsonPath("$.coupons").isArray())
                .andExpect(jsonPath("$.coupons.length()").value(2));

        verify(couponService).findByCreatedBy(eq(mockUser), any(Pageable.class));
    }

    @Test
    @WithMockUser(username = "invalidRoleUser", roles = "구매자")
    public void findListBySeller_UnauthorizedRole() throws Exception {
        // Given
        Long sellerId = 1L;
        mockUser.setRole(UserRole.ROLE_자영업자);
        when(userAuthValidator.getCurrentUserByUserID(eq(sellerId))).thenReturn(mockUser);

        // When & Then
        mockMvc.perform(get("/api/coupon/seller/{sellerID}", sellerId))
                .andExpect(status().isUnauthorized())
                .andExpect(content().json("{\"message\":\"잘못된 요청입니다.\"}"));
    }
}
