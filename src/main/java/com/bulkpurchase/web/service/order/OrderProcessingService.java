package com.bulkpurchase.web.service.order;

import com.bulkpurchase.domain.dto.order.OrderItemDTO;
import com.bulkpurchase.domain.dto.order.OrderRequestDTO;
import com.bulkpurchase.domain.entity.cart.CartItem;
import com.bulkpurchase.domain.entity.order.Order;
import com.bulkpurchase.domain.entity.order.Payment;
import com.bulkpurchase.domain.entity.product.Product;
import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.service.cart.CartItemService;
import com.bulkpurchase.domain.service.cart.CartService;
import com.bulkpurchase.domain.service.order.OrderDetailService;
import com.bulkpurchase.domain.service.order.OrderService;
import com.bulkpurchase.domain.service.order.PaymentService;
import com.bulkpurchase.domain.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderProcessingService {

    private final ProductService productService;
    private final OrderDetailService orderDetailService;
    private final PaymentService paymentService;
    private final OrderService orderService;
    private final CartService cartService;
    private final CartItemService cartItemService;

    @Transactional
    public Order processOrder(OrderRequestDTO orderRequestDTO, User user) {
        List<OrderItemDTO> orderItemDTOS = orderRequestDTO.getOrderItemDTOS();

        Order order = new Order(user, orderRequestDTO.getTotalPrice());
        Order savedOrder = orderService.save(order);

        orderItemDTOS.forEach(orderItemDTO -> {
            Product product = productService.findById(orderItemDTO.getProductID())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "상품이 존재하지 않습니다."));
            orderDetailService.save(order, product, orderItemDTO.getQuantity());
        });

        return savedOrder;
    }

    public Payment processPayment(OrderRequestDTO orderRequestDTO, Order order) {
        String paymentMethod = orderRequestDTO.getPaymentMethod();
        Payment payment = new Payment(order, orderRequestDTO.getTotalPrice(), paymentMethod);
        if (paymentMethod.equals("Credit Card") || paymentMethod.equals("Kakao Pay")) {
            payment.setStatus("결제 완료");
        } else if (paymentMethod.equals("Bank Transfer")) {
            payment.setStatus("보류");
        }
        return paymentService.save(payment);
    }

    public void setTotalPrice(OrderRequestDTO orderRequestDTO) {
        List<OrderItemDTO> orderItemDTOS = orderRequestDTO.getOrderItemDTOS();
        double totalPrice = 0D;
        for (OrderItemDTO orderItemDTO : orderItemDTOS) {
            Double price = productService.findById(orderItemDTO.getProductID())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "상품이 존재하지 않습니다.")).getPrice();
            totalPrice += price * orderItemDTO.getQuantity();
        }
        orderRequestDTO.setTotalPrice(totalPrice);
    }

    public void cartDelete_AfterOrderComplete(User user, OrderRequestDTO orderRequestDTO) {
        cartService.findByUser(user).ifPresent(cart -> {
            Set<Long> orderProductIds = orderRequestDTO.getOrderItemDTOS().stream()
                    .map(OrderItemDTO::getProductID)
                    .collect(Collectors.toSet());

            List<CartItem> itemsToDelete = cart.getItems().stream()
                    .filter(item -> orderProductIds.contains(item.getProduct().getProductID()))
                    .toList();

            cartItemService.deleteAll(itemsToDelete);
        });
    }
}
