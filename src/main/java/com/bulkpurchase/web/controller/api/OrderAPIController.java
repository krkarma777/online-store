package com.bulkpurchase.web.controller.api;

import com.bulkpurchase.domain.dto.cart.CartItemOrderResponseDTO;
import com.bulkpurchase.domain.dto.order.OrderDetailResponseDTO;
import com.bulkpurchase.domain.dto.order.OrderResponseDTO;
import com.bulkpurchase.domain.dto.order.OrderViewDTO;
import com.bulkpurchase.domain.dto.order.PaymentResponseDTO;
import com.bulkpurchase.domain.entity.cart.Cart;
import com.bulkpurchase.domain.entity.order.Order;
import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.service.cart.CartService;
import com.bulkpurchase.domain.service.order.OrderDetailService;
import com.bulkpurchase.domain.service.order.OrderService;
import com.bulkpurchase.domain.service.order.PaymentService;
import com.bulkpurchase.domain.validator.user.UserAuthValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private final CartService cartService;

    @GetMapping("/list")
    public ResponseEntity<?> orderList(Principal principal) {
        User user = userAuthValidator.getCurrentUser(principal);
        List<OrderViewDTO> orderViewDTOS = orderService.getOrderViewModelsByUser(user);
        return ResponseEntity.status(HttpStatus.OK).body(orderViewDTOS);
    }

    @GetMapping("/{orderID}")
    public ResponseEntity<?> orderDetail(@PathVariable("orderID") Long orderID) {
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

    @GetMapping("/cart/{cartID}")
    public ResponseEntity<?> orderForm(@PathVariable(value = "cartID") Long cartID, @RequestParam("itemIDs") List<Long> itemIDs, Principal principal) {
        Cart cart = cartService.findById(cartID).orElse(null);
        if (cart == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "장바구니가 존재하지 않습니다."));
        }

        User user = userAuthValidator.getCurrentUser(principal);
        if (!cart.getUser().equals(user)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "잘못된 요청입니다."));
        }

        List<CartItemOrderResponseDTO> orderResponseDTOS = cart.getItems().stream()
                .filter(cartItem -> itemIDs.contains(cartItem.getCartItemID()))
                .map(CartItemOrderResponseDTO::new)
                .toList();

        return ResponseEntity.ok(orderResponseDTOS);
    }
}
