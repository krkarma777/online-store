package com.bulkpurchase.web.controller.api;

import com.bulkpurchase.domain.dto.user.FavoriteProductDTO;
import com.bulkpurchase.domain.entity.product.Product;
import com.bulkpurchase.domain.entity.user.FavoriteProduct;
import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.service.product.ProductService;
import com.bulkpurchase.domain.service.user.FavoriteProductService;
import com.bulkpurchase.domain.validator.user.UserAuthValidator;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class FavoriteAPIControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserAuthValidator userAuthValidator;

    @MockBean
    private FavoriteProductService favoriteProductService;

    @MockBean
    private ProductService productService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
    }

    @Test
    @WithMockUser
    public void testToggleFavorite() throws Exception {
        User user = new User();
        Product product = new Product();
        product.setProductID(1L);
        when(userAuthValidator.getCurrentUser(any())).thenReturn(user);
        when(productService.findById(anyLong())).thenReturn(Optional.of(product));
        when(favoriteProductService.toggleFavorite(any(User.class), any(Product.class))).thenReturn(true);

        mockMvc.perform(post("/api/favorite?productID=1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.isFavorited").value(true));
    }

    @Test
    @WithMockUser
    public void testDeleteFavoriteList() throws Exception {
        Product product = new Product();
        product.setProductID(1L);

        when(userAuthValidator.getCurrentUser(any())).thenReturn(user);
        when(productService.findById(anyLong())).thenReturn(Optional.of(product));
        when(favoriteProductService.toggleFavorite(any(User.class), any(Product.class)))
                .thenReturn(true);

        mockMvc.perform(delete("/api/favorite/list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"productIDs\":[1]}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("선택한 찜 상품이 삭제되었습니다."));
    }

    @Test
    @WithMockUser
    public void testFavoriteList() throws Exception {
        List<FavoriteProduct> favoriteProductList = List.of(new FavoriteProduct());
        Page<FavoriteProduct> favoriteProducts = new PageImpl<>(favoriteProductList, PageRequest.of(0, 10, Sort.Direction.DESC, "id"), 1);

        when(userAuthValidator.getCurrentUser(any())).thenReturn(user);
        when(favoriteProductService.findByUser(eq(user), any(PageRequest.class))).thenReturn(favoriteProducts);

        mockMvc.perform(get("/api/favorite/list?page=1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.favoriteProductDTOs").isArray())
                .andExpect(jsonPath("$.favoriteProductDTOs[0]").isNotEmpty());
    }
    @Test
    @WithMockUser
    public void testFavoriteOne() throws Exception {
        FavoriteProduct favoriteProduct = new FavoriteProduct();
        favoriteProduct.setId(1L);

        when(userAuthValidator.getCurrentUser(any())).thenReturn(user);
        when(favoriteProductService.findByUserAndId(any(User.class), anyLong())).thenReturn(Optional.of(favoriteProduct));

        mockMvc.perform(get("/api/favorite?id=1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1));
    }

}

