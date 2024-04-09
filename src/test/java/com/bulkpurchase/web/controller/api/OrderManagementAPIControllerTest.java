package com.bulkpurchase.web.controller.api;

import com.bulkpurchase.domain.dto.orderdetail.OrderDetailManagementResponseDTO;
import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.enums.UserRole;
import com.bulkpurchase.domain.service.order.OrderDetailService;
import com.bulkpurchase.domain.validator.user.UserAuthValidator;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class OrderManagementAPIControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderDetailService orderDetailService;

    @MockBean
    private UserAuthValidator userAuthValidator;

    @Autowired
    private ObjectMapper objectMapper;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUsername("testUser");
        user.setRole(UserRole.ROLE_판매자);
        given(userAuthValidator.getCurrentUser(any(Principal.class))).willReturn(user);
    }
    @Test
    @WithMockUser(username = "seller", roles = {"SELLER"})
    void manageForm_WhenRequestedBySeller_ShouldReturnOrderDetails() throws Exception {
        Sort sort = Sort.by(Sort.Direction.DESC, "orderDetailID");
        Pageable pageable = PageRequest.of(0, 10, sort);

        List<OrderDetailManagementResponseDTO> list = new ArrayList<>();
        Map<String, Object> responseData = Map.of("orderDetails", list, "totalPages", 10);

        given(orderDetailService.findBySeller(user, pageable)).willReturn(responseData);

        mockMvc.perform(get("/api/order-management?page=1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderDetails").value(list))
                .andExpect(jsonPath("$.totalPages").value(10));
    }
}