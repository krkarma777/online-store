package com.bulkpurchase.web.controller.api;

import com.bulkpurchase.domain.dto.productInquiry.ProductInquiryCreateRequestDTO;
import com.bulkpurchase.domain.dto.productInquiry.ProductInquiryUpdateRequestDTO;
import com.bulkpurchase.domain.entity.product.Product;
import com.bulkpurchase.domain.entity.product.ProductInquiry;
import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.service.product.ProductInquiryService;
import com.bulkpurchase.domain.service.product.ProductService;
import com.bulkpurchase.domain.validator.user.UserAuthValidator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.Date;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ProductInquiryAPIControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserAuthValidator userAuthValidator;

    @MockBean
    private ProductInquiryService productInquiryService;

    @MockBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    private User user;
    private ProductInquiryCreateRequestDTO createRequestDTO;
    private ProductInquiryUpdateRequestDTO updateRequestDTO;
    private Product product;
    private ProductInquiry productInquiry;

    @BeforeEach
    void setUp() {
        user = new User();
        createRequestDTO = new ProductInquiryCreateRequestDTO();
        createRequestDTO.setProductID(1L);
        createRequestDTO.setInquiryContent("test inquiry");

        product = new Product();
        product.setProductID(1L);

        productInquiry = new ProductInquiry(createRequestDTO, product, user);
        productInquiry.setInquiryID(1L);

        updateRequestDTO = new ProductInquiryUpdateRequestDTO();
        updateRequestDTO.setInquiryID(1L);
        updateRequestDTO.setInquiryContent("updated inquiry content");
    }
    @Test
    @WithMockUser
    public void testCreateSuccessfully() throws Exception {
        given(productService.findById(1L)).willReturn(Optional.of(product));
        given(userAuthValidator.getCurrentUser(any(Principal.class))).willReturn(user);
        given(productInquiryService.save(any(ProductInquiry.class))).willReturn(productInquiry);

        mockMvc.perform(post("/api/product-inquiry")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.inquiryID").value(1L))
                .andExpect(jsonPath("$.message").value("문의가 정상적으로 등록되었습니다."));
    }

    @Test
    @WithMockUser
    public void testCreateForNonexistentProduct() throws Exception {
        given(userAuthValidator.getCurrentUser(any(Principal.class))).willReturn(user);
        given(productInquiryService.save(any(ProductInquiry.class))).willReturn(productInquiry);

        mockMvc.perform(post("/api/product-inquiry")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequestDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("상품이 존재하지 않습니다."));
    }

    @Test
    @WithMockUser
    public void testUpdateSuccessfully() throws Exception {
        given(productInquiryService.findById(1L)).willReturn(Optional.of(productInquiry));
        given(userAuthValidator.getCurrentUser(any(Principal.class))).willReturn(user);

        mockMvc.perform(patch("/api/product-inquiry")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.inquiryID").value(1L))
                .andExpect(jsonPath("$.message").value("문의 수정이 완료되었습니다."));
    }

    @Test
    @WithMockUser
    public void testUpdateForNonexistentInquiry() throws Exception {
        given(productInquiryService.findById(1L)).willReturn(Optional.empty());

        mockMvc.perform(patch("/api/product-inquiry")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequestDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("문의가 존재하지 않습니다."));
    }

    @Test
    @WithMockUser
    public void testUpdateForUnauthorizedUser() throws Exception {
        productInquiry.setUser(new User());

        given(productInquiryService.findById(1L)).willReturn(Optional.of(productInquiry));
        given(userAuthValidator.getCurrentUser(any(Principal.class))).willReturn(user);

        mockMvc.perform(patch("/api/product-inquiry")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequestDTO)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("글을 수정할 권한이 없습니다."));
    }

    @Test
    @WithMockUser
    public void testUpdateForRepliedInquiry() throws Exception {
        productInquiry.setReplyDate(new Date());

        given(productInquiryService.findById(1L)).willReturn(Optional.of(productInquiry));
        given(userAuthValidator.getCurrentUser(any(Principal.class))).willReturn(user);

        mockMvc.perform(patch("/api/product-inquiry")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("답변이 달린 글은 수정할 수 없습니다."));
    }

    @Test
    @WithMockUser
    public void testUpdateWithInvalidRequest() throws Exception {
        mockMvc.perform(patch("/api/product-inquiry")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."));
    }
}
