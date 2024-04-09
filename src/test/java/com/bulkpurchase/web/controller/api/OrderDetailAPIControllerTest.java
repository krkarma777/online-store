package com.bulkpurchase.web.controller.api;

import com.bulkpurchase.domain.dto.order.OrderDetailResponseDTO;
import com.bulkpurchase.domain.dto.order.OrderResponseDTO;
import com.bulkpurchase.domain.dto.order.PaymentResponseDTO;
import com.bulkpurchase.domain.dto.orderdetail.OrderDetailManagementResponseDTO;
import com.bulkpurchase.domain.dto.orderdetail.OrderDetailStatusUpdateRequestDTO;
import com.bulkpurchase.domain.entity.order.Order;
import com.bulkpurchase.domain.entity.order.OrderDetail;
import com.bulkpurchase.domain.entity.order.Payment;
import com.bulkpurchase.domain.entity.product.Product;
import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.enums.OrderStatus;
import com.bulkpurchase.domain.enums.UserRole;
import com.bulkpurchase.domain.service.order.OrderDetailService;
import com.bulkpurchase.domain.service.order.PaymentService;
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
import java.util.Optional;

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
class OrderDetailAPIControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderDetailService orderDetailService;

    @MockBean
    private PaymentService paymentService;

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

        mockMvc.perform(get("/api/order-detail/seller?page=1")
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

        mockMvc.perform(patch("/api/order-detail")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk());

        verify(orderStatusUpdateService).updateOrderStatus(eq(orderDetail), eq(OrderStatus.PROCESSING));
    }

    @Test
    void orderDetailInfoForSeller_ShouldReturnOrderDetailInfo() throws Exception {
        Long orderDetailID = 1L;

        OrderDetail mockOrderDetail = createMockOrderDetail();
        Payment payment = new Payment();

        given(orderDetailService.findByID(eq(orderDetailID))).willReturn(Optional.of(mockOrderDetail));
        given(paymentService.findByOrder(eq(mockOrderDetail.getOrder()))).willReturn(payment);

        OrderDetailResponseDTO orderDetailResponseDTO = new OrderDetailResponseDTO(mockOrderDetail);
        OrderResponseDTO orderResponseDTO = new OrderResponseDTO(mockOrderDetail.getOrder());
        PaymentResponseDTO paymentResponseDTO = new PaymentResponseDTO(payment);

        mockMvc.perform(get("/api/order-detail/{orderDetailID}", orderDetailID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderDetail.productName").value(orderDetailResponseDTO.getProductName()))
                .andExpect(jsonPath("$.orderDetail.quantity").value(orderDetailResponseDTO.getQuantity()))
                .andExpect(jsonPath("$.orderDetail.price").value(orderDetailResponseDTO.getPrice()))
                .andExpect(jsonPath("$.orderDetail.shippingCompany").value(orderDetailResponseDTO.getShippingCompany()))
                .andExpect(jsonPath("$.orderDetail.shippingNumber").value(orderDetailResponseDTO.getShippingNumber()))
                .andExpect(jsonPath("$.orderDetail.orderDetailStatus").value(orderDetailResponseDTO.getOrderDetailStatus().toString()))
                .andExpect(jsonPath("$.order.orderID").value(orderResponseDTO.getOrderID()))
                .andExpect(jsonPath("$.payment.paymentDate").value(paymentResponseDTO.getPaymentDate()));
    }

    private OrderDetail createMockOrderDetail() {
        Long orderDetailID = 1L;
        Order order = new Order();
        order.setOrderID(1L);

        Product product = new Product();
        product.setProductID(1L);

        Integer quantity = 5;
        Double price = 99.99;
        String shippingCompany = "FastShip";
        String shippingNumber = "FS123456789";
        OrderStatus status = OrderStatus.PENDING;

        return new OrderDetail(orderDetailID, order, product, quantity, price, shippingCompany, shippingNumber, status);
    }
}