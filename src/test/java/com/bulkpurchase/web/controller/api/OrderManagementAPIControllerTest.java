package com.bulkpurchase.web.controller.api;

import com.bulkpurchase.domain.dto.orderdetail.OrderDetailManagementResponseDTO;
import com.bulkpurchase.domain.dto.orderdetail.OrderDetailStatusUpdateRequestDTO;
import com.bulkpurchase.domain.entity.order.OrderDetail;
import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.enums.OrderStatus;
import com.bulkpurchase.domain.enums.UserRole;
import com.bulkpurchase.domain.service.order.OrderDetailService;
import com.bulkpurchase.domain.validator.user.UserAuthValidator;
import com.bulkpurchase.web.service.order.OrderStatusUpdateService;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
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

    @MockBean
    private OrderStatusUpdateService orderStatusUpdateService;

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

    @Test
    @WithMockUser(username = "seller", roles = {"SELLER"})
    void orderStatusChange_WhenRequestedBySeller_ShouldUpdateOrderStatus() throws Exception {
        OrderDetailStatusUpdateRequestDTO requestDTO = new OrderDetailStatusUpdateRequestDTO();
        requestDTO.setOrderDetailID(1L);
        requestDTO.setOrderStatus(OrderStatus.PROCESSING);

        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrderDetailID(1L);
        orderDetail.setStatus(OrderStatus.PENDING);

        given(orderDetailService.findByID(eq(1L))).willReturn(java.util.Optional.of(orderDetail));

        mockMvc.perform(patch("/api/order-management")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk());

        verify(orderStatusUpdateService).updateOrderStatus(eq(orderDetail), eq(OrderStatus.PROCESSING));
    }
}