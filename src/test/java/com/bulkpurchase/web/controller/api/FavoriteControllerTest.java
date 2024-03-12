package com.bulkpurchase.web.controller.api;

import com.bulkpurchase.domain.entity.product.Product;
import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.service.product.ProductService;
import com.bulkpurchase.domain.service.user.FavoriteProductService;
import com.bulkpurchase.domain.validator.user.UserAuthValidator;
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

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class FavoriteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserAuthValidator userAuthValidator;

    @MockBean
    private FavoriteProductService favoriteProductService;

    @MockBean
    private ProductService productService;

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
}

