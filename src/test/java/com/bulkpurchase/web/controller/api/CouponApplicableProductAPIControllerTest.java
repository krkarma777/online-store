package com.bulkpurchase.web.controller.api;

import com.bulkpurchase.domain.dto.coupon.CouponApplicableProductRequestDTO;
import com.bulkpurchase.domain.dto.product.ProductForCouponDTO;
import com.bulkpurchase.domain.entity.coupon.Coupon;
import com.bulkpurchase.domain.entity.coupon.CouponApplicableProduct;
import com.bulkpurchase.domain.entity.product.Category;
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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
    private CouponService couponService;

    @MockBean
    private UserAuthValidator userAuthValidator;

    @MockBean
    private CouponApplicableProductService couponApplicableProductService;

    @MockBean
    private ProductService productService;

    private User user;
    private Coupon coupon;
    private Product product1;
    private Product product2;

    @BeforeEach
    void setUp() {
        user = new User();
        coupon = new Coupon(1L, user);
        product1 = new Product(1L, user);
        product2 = new Product(2L, new User()); // 다른 사용자가 게시한 상품
    }

    @Test
    public void findApplicableProductsForCoupon_ShouldReturnProductList() throws Exception {
        ProductForCouponDTO dto1 = new ProductForCouponDTO(1L, "Product 1");
        ProductForCouponDTO dto2 = new ProductForCouponDTO(2L, "Product 2");
        List<CouponApplicableProduct> mockData = List.of(
                new CouponApplicableProduct(coupon, dto1.getProductID()),
                new CouponApplicableProduct(coupon, dto2.getProductID())
        );

        given(couponApplicableProductService.findByCouponCouponID(anyLong())).willReturn(mockData);
        given(productService.findById(1L)).willReturn(Optional.of(new Product(dto1.getProductID(), dto1.getProductName(), new Category())));
        given(productService.findById(2L)).willReturn(Optional.of(new Product(dto2.getProductID(), dto2.getProductName(), new Category())));

        mockMvc.perform(get("/api/coupon-applicable-product/{couponID}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // 응답 본문에 포함된 객체의 수 검증
                .andExpect(jsonPath("$", hasSize(2)))
                // 첫 번째 객체의 필드 값 검증
                .andExpect(jsonPath("$[0].productID", is(dto1.getProductID().intValue())))
                .andExpect(jsonPath("$[0].productName", is(dto1.getProductName())))
                // 두 번째 객체의 필드 값 검증
                .andExpect(jsonPath("$[1].productID", is(dto2.getProductID().intValue())))
                .andExpect(jsonPath("$[1].productName", is(dto2.getProductName())));
    }

    @Test
    @WithMockUser(username="user1")
    public void selectProductForCoupon_UnauthorizedUser_ThrowsUnauthorized() throws Exception {
        given(userAuthValidator.getCurrentUser(any(Principal.class))).willReturn(new User()); // 다른 사용자 객체 반환
        given(couponService.findById(any(Long.class))).willReturn(Optional.of(coupon));

        CouponApplicableProductRequestDTO requestDTO = new CouponApplicableProductRequestDTO();
        requestDTO.setCouponID(1L);
        requestDTO.setProductIDs(List.of(1L));

        mockMvc.perform(post("/api/coupon-applicable-product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("권한이 없습니다."));
    }

    @Test
    @WithMockUser(username="user1")
    public void selectProductForCoupon_ProductNotOwnedByUser_ThrowsUnauthorized() throws Exception {
        given(userAuthValidator.getCurrentUser(any(Principal.class))).willReturn(user);
        given(couponService.findById(any(Long.class))).willReturn(Optional.of(coupon));
        given(productService.findById(1L)).willReturn(Optional.of(product1));
        given(productService.findById(2L)).willReturn(Optional.of(product2)); // 다른 사용자가 게시한 상품

        CouponApplicableProductRequestDTO requestDTO = new CouponApplicableProductRequestDTO();
        requestDTO.setCouponID(1L);
        requestDTO.setProductIDs(List.of(1L, 2L));

        mockMvc.perform(post("/api/coupon-applicable-product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("하나 이상의 상품이 사용자에 의해 게시되지 않았습니다."));
    }

    @Test
    @WithMockUser
    public void selectProductForCoupon_InvalidProductInfo_ThrowsNotFound() throws Exception {
        // 상품 정보가 유효하지 않은 경우 설정
        given(userAuthValidator.getCurrentUser(any(Principal.class))).willReturn(user);
        given(couponService.findById(1L)).willReturn(Optional.of(coupon));
        given(productService.findById(1L)).willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "잘못된 상품 정보입니다."));

        CouponApplicableProductRequestDTO requestDTO = new CouponApplicableProductRequestDTO();
        requestDTO.setCouponID(1L);
        requestDTO.setProductIDs(List.of(1L)); // 잘못된 상품 ID

        mockMvc.perform(post("/api/coupon-applicable-product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("잘못된 상품 정보입니다."));
    }

    @Test
    @WithMockUser
    public void selectProductForCoupon_CouponDoesNotExist_ThrowsNotFound() throws Exception {
        // 쿠폰이 존재하지 않는 경우 설정
        given(couponService.findById(1L)).willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 쿠폰입니다."));

        CouponApplicableProductRequestDTO requestDTO = new CouponApplicableProductRequestDTO();
        requestDTO.setCouponID(1L); // 존재하지 않는 쿠폰 ID
        requestDTO.setProductIDs(List.of(1L, 2L, 3L));

        mockMvc.perform(post("/api/coupon-applicable-product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("존재하지 않는 쿠폰입니다."));
    }

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
