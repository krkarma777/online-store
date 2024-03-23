package com.bulkpurchase.web.controller.api;

import com.bulkpurchase.domain.dto.product.CreateProductDTO;
import com.bulkpurchase.domain.entity.product.Product;
import com.bulkpurchase.domain.entity.user.User;
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
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    private UserAuthValidator userAuthValidator; // 가정한 UserAuthValidator MockBean

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext) {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @WithMockUser(username = "qweqwe", roles = "판매자")
    void whenCreateProductWithValidInput_thenReturnsStatusOk() throws Exception {
        // Given
        CreateProductDTO createProductDTO = new CreateProductDTO();
        createProductDTO.setProductName("Test Product");
        createProductDTO.setDescription("Test Description");
        createProductDTO.setPrice(5000.0);
        createProductDTO.setStock(10);
        createProductDTO.setCategoryID(1L);

        User user = new User();
        user.setUsername("testUser");

        // 목 Principal 객체 생성
        Principal mockPrincipal = () -> "qweqwe";

        // Product 객체에 productID 설정
        Product mockedProduct = new Product(createProductDTO, user);
        mockedProduct.setProductID(1L); // 여기서 ID를 설정합니다.

        given(userAuthValidator.getCurrentUser(any(Principal.class))).willReturn(user);
        given(productService.save(any(Product.class))).willReturn(mockedProduct);


        // When & Then
        mockMvc.perform(post("/api/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createProductDTO))
                        .principal(mockPrincipal)) // 목 Principal 객체 사용
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("상품 등록에 성공하셨습니다."))
                .andExpect(jsonPath("$.productID").value(1));
    }


    @Test
    @WithMockUser(username = "qweqwe", roles = "판매자")
    void whenCreateProductWithInvalidInput_thenReturnsBadRequest() throws Exception {
        // Given
        CreateProductDTO createProductDTO = new CreateProductDTO();

        // When & Then
        mockMvc.perform(post("/api/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createProductDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("잘못된 정보를 기입하셨습니다."));
    }
}