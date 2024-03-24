package com.bulkpurchase.web.controller.api;

import com.bulkpurchase.domain.dto.product.ProductRequestDTO;
import com.bulkpurchase.domain.entity.product.Category;
import com.bulkpurchase.domain.entity.product.Product;
import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.service.product.ProductService;
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

import java.security.Principal;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ProductAPIControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    @MockBean
    private UserAuthValidator userAuthValidator;

    @Test
    @WithMockUser(username = "qweqwe", roles = "판매자")
    void whenCreateProductWithValidInput_thenReturnsStatusOk() throws Exception {
        // Given
        ProductRequestDTO productRequestDTO = new ProductRequestDTO();
        productRequestDTO.setProductName("Test Product");
        productRequestDTO.setDescription("Test Description");
        productRequestDTO.setPrice(5000.0);
        productRequestDTO.setStock(10);
        productRequestDTO.setCategoryID(1L);

        User user = new User();
        user.setUsername("testUser");

        Principal mockPrincipal = () -> "qweqwe";

        // Product 객체에 productID 설정
        Product mockedProduct = new Product(productRequestDTO, user);
        mockedProduct.setProductID(1L); // 여기서 ID를 설정합니다.

        given(userAuthValidator.getCurrentUser(any(Principal.class))).willReturn(user);
        given(productService.save(any(Product.class))).willReturn(mockedProduct);

        // When & Then
        mockMvc.perform(post("/api/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productRequestDTO))
                        .principal(mockPrincipal)) // 목 Principal 객체 사용
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("상품 등록에 성공하셨습니다."))
                .andExpect(jsonPath("$.productID").value(1));
    }


    @Test
    @WithMockUser(username = "qweqwe", roles = "판매자")
    void whenCreateProductWithInvalidInput_thenReturnsBadRequestWithErrors() throws Exception {
        // Given
        ProductRequestDTO productRequestDTO = new ProductRequestDTO();
        // 입력값을 빈 상태로 둠

        // When & Then
        mockMvc.perform(post("/api/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productRequestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("입력 값에 오류가 있습니다.")) // 메시지 변경
                .andExpect(jsonPath("$.errors").exists()); // 오류 디테일 확인
    }

    @Test
    @WithMockUser(username = "qweqwe", roles = "판매자")
    void whenUpdateProductWithValidInputAndAuthorizedUser_thenReturnsStatusOk() throws Exception {
        Long productId = 1L;
        ProductRequestDTO productRequestDTO = createValidProductRequestDTO();

        User user = new User();
        user.setUserID(1L); // 사용자 ID 설정
        user.setUsername("qweqwe");
        System.out.println("user = " + user);

        Product product = new Product();
        product.setProductID(productId);
        product.setUser(user);

        given(userAuthValidator.getCurrentUser(any(Principal.class))).willReturn(user);
        given(productService.findById(productId)).willReturn(Optional.of(product));
        given(productService.save(any(Product.class))).willReturn(product);

        mockMvc.perform(patch("/api/product/{productID}", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("상품 수정에 성공하셨습니다."));
    }

    @Test
    @WithMockUser(username = "otherUser", roles = "판매자")
    void whenUpdateProductWithUnauthorizedUser_thenReturnsStatusUnauthorized() throws Exception {
        Long productId = 1L;
        ProductRequestDTO productRequestDTO = createValidProductRequestDTO();

        User ownerUser = new User();
        ownerUser.setUserID(1L); // 소유자 사용자 ID 설정
        ownerUser.setUsername("testUser");

        Product product = new Product();
        product.setProductID(productId);
        product.setUser(ownerUser);

        User otherUser = new User();
        otherUser.setUserID(2L); // 다른 사용자 ID 설정
        otherUser.setUsername("otherUser");

        given(productService.findById(productId)).willReturn(Optional.of(product));
        given(userAuthValidator.getCurrentUser(any(Principal.class))).willReturn(otherUser);

        mockMvc.perform(patch("/api/product/{productID}", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productRequestDTO)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("상품을 수정할 권한이 없습니다."));
    }


    @Test
    @WithMockUser(username = "testUser", roles = "판매자")
    void whenUpdateNonExistingProduct_thenReturnsStatusNotFound() throws Exception {
        Long nonExistingProductId = 999L;
        ProductRequestDTO productRequestDTO = createValidProductRequestDTO();

        given(productService.findById(nonExistingProductId)).willReturn(Optional.empty());

        mockMvc.perform(patch("/api/product/{productID}", nonExistingProductId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productRequestDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("존재하지 않는 상품입니다."));
    }

    private ProductRequestDTO createValidProductRequestDTO() {
        ProductRequestDTO dto = new ProductRequestDTO();
        dto.setProductName("Valid Product Name");
        dto.setDescription("Valid Description");
        dto.setPrice(5000.0);
        dto.setStock(10);
        dto.setCategoryID(1L);
        return dto;
    }

    @Test
    @WithMockUser(username = "testUser", roles = "판매자")
    void whenReadOneExistingProduct_thenReturnsProductInfo() throws Exception {
        Long existingProductId = 1L;
        Product existingProduct = new Product();
        existingProduct.setProductID(existingProductId);
        existingProduct.setProductName("Existing Product");
        Category category = new Category();
        category.setCategoryID(1L);
        category.setName("test category");
        existingProduct.setCategory(category);

        given(productService.findById(existingProductId)).willReturn(Optional.of(existingProduct));

        mockMvc.perform(get("/api/product/{productID}", existingProductId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.product.productName").value(existingProduct.getProductName()));
    }

    @Test
    @WithMockUser(username = "testUser", roles = "판매자")
    void whenReadOneNonExistingProduct_thenReturnsNotFoundStatus() throws Exception {
        Long nonExistingProductId = 999L;
        given(productService.findById(nonExistingProductId)).willReturn(Optional.empty());

        mockMvc.perform(get("/api/product/{productID}", nonExistingProductId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("존재하지 않는 상품입니다."));
    }

    @Test
    @WithMockUser(username = "testUser", roles = "판매자")
    void authorizedCheck_ProductNotFound() throws Exception {
        given(productService.findById(anyLong())).willReturn(Optional.empty());

        mockMvc.perform(get("/api/product/authorizedCheck/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("존재하지 않는 상품입니다."));
    }

    // 상품이 존재하며, 사용자가 수정 권한을 가지고 있는 경우
    @Test
    @WithMockUser(username = "user1", roles = "판매자")
    void authorizedCheck_UserHasPermission() throws Exception {
        Product mockProduct = new Product();
        User mockUser = new User();
        mockUser.setUsername("user1"); // 현재 인증된 사용자
        mockProduct.setUser(mockUser); // 상품 소유자

        Category mockCategory = new Category();
        mockCategory.setCategoryID(1L);
        mockProduct.setCategory(mockCategory);

        given(productService.findById(1L)).willReturn(Optional.of(mockProduct));
        given(userAuthValidator.getCurrentUser(any())).willReturn(mockUser); // 현재 인증된 사용자 반환

        mockMvc.perform(get("/api/product/authorizedCheck/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.product").exists());
    }

    // 상품이 존재하지만, 사용자가 수정 권한을 가지고 있지 않은 경우
    @Test
    @WithMockUser(username = "user2", roles = "판매자")
    void authorizedCheck_UserLacksPermission() throws Exception {
        Product mockProduct = new Product();
        User ownerUser = new User();
        ownerUser.setUsername("user1"); // 상품 소유자
        mockProduct.setUser(ownerUser);

        User currentUser = new User();
        currentUser.setUsername("user2"); // 테스트를 위한 다른 사용자

        given(productService.findById(1L)).willReturn(Optional.of(mockProduct));
        given(userAuthValidator.getCurrentUser(any())).willReturn(currentUser); // 현재 인증된 다른 사용자 반환

        mockMvc.perform(get("/api/product/authorizedCheck/1"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("상품을 수정할 권한이 없습니다."));
    }
}