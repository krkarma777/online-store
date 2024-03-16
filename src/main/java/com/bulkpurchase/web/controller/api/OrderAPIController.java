package com.bulkpurchase.web.controller.api;

import com.bulkpurchase.domain.dto.order.OrderDetailResponseDTO;
import com.bulkpurchase.domain.dto.order.OrderResponseDTO;
import com.bulkpurchase.domain.dto.order.OrderViewDTO;
import com.bulkpurchase.domain.dto.order.PaymentResponseDTO;
import com.bulkpurchase.domain.dto.orderdetail.OrderDetailViewDTO;
import com.bulkpurchase.domain.entity.order.Order;
import com.bulkpurchase.domain.entity.order.OrderDetail;
import com.bulkpurchase.domain.entity.order.Payment;
import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.service.order.OrderDetailService;
import com.bulkpurchase.domain.service.order.OrderService;
import com.bulkpurchase.domain.service.order.PaymentService;
import com.bulkpurchase.domain.validator.user.UserAuthValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/order")
public class OrderAPIController {

    private final OrderService orderService;
    private final OrderDetailService orderDetailService;
    private final PaymentService paymentService;
    private final UserAuthValidator userAuthValidator;

    @GetMapping("/list")
    public ResponseEntity<?> myPageForm(Principal principal) {
        User user = userAuthValidator.getCurrentUser(principal);
        List<OrderViewDTO> orderViewDTOS = orderService.getOrderViewModelsByUser(user);
        return ResponseEntity.status(HttpStatus.OK).body(orderViewDTOS);
    }

    @GetMapping("/{orderID}")
    public ResponseEntity<?> orderDetailInfoForSeller(@PathVariable("orderID") Long orderID) {
        Optional<Order> orderOpt = orderService.findById(orderID);
        if (orderOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "잘못된 요청입니다."));
        }
        Order order = orderOpt.get();
        OrderResponseDTO orderResponseDTO = new OrderResponseDTO(order);
        PaymentResponseDTO paymentResponseDTO = new PaymentResponseDTO(paymentService.findByOrder(order));
        List<OrderDetailResponseDTO> detailResponseDTOS = orderDetailService.findByOrder(order).stream().map(OrderDetailResponseDTO::new).toList();

        return ResponseEntity.ok(Map.of("orderDetailList", detailResponseDTOS,"order", orderResponseDTO,"payment", paymentResponseDTO));
    }
}
