package com.bulkpurchase.web.controller.seller;

import com.bulkpurchase.domain.entity.order.Order;
import com.bulkpurchase.domain.entity.order.OrderDetail;
import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.enums.OrderStatus;
import com.bulkpurchase.domain.service.order.OrderDetailService;
import com.bulkpurchase.domain.service.order.OrderService;
import com.bulkpurchase.domain.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping
@RequiredArgsConstructor
public class OrderManageController {

    private final OrderService orderService;
    private final UserService userService;
    private final OrderDetailService orderDetailService;

    @PostMapping("/orders/edit/{orderDetailID}")
    public String orderStatusChange(@PathVariable(value = "orderDetailID") Long orderDetailID, Principal principal,
                                    @RequestParam(value = "orderStatus") OrderStatus status) {
        User user = userService.findByUsername(principal.getName());
        Optional<OrderDetail> orderDetailOpt = orderDetailService.findByID(orderDetailID);

        if (orderDetailOpt.isPresent()) {
            OrderDetail orderDetail = orderDetailOpt.get();
            if (orderDetail.getProduct().getUser().equals(user)) {
                orderDetail.setStatus(status);
                orderDetailService.save(orderDetail);

                Order order = orderDetail.getOrder();
                List<OrderDetail> details = orderDetailService.findByOrder(order);
                // 주문 상태 업데이트 로직 시작
                boolean allDelivered = details.stream().allMatch(d -> d.getStatus() == OrderStatus.DELIVERED);
                boolean anyCancelled = details.stream().anyMatch(d -> d.getStatus() == OrderStatus.CANCELLED);
                boolean anyShipped = details.stream().anyMatch(d -> d.getStatus() == OrderStatus.SHIPPED);
                boolean allProcessingOrShipped = details.stream().allMatch(d ->
                        d.getStatus() == OrderStatus.PROCESSING || d.getStatus() == OrderStatus.SHIPPED);

                if (anyCancelled) {
                    order.setStatus(OrderStatus.CANCELLED);
                } else if (allDelivered) {
                    order.setStatus(OrderStatus.DELIVERED);
                } else if (anyShipped && allProcessingOrShipped) {
                    order.setStatus(OrderStatus.SHIPPED);
                } else {
                    order.setStatus(OrderStatus.PROCESSING);
                }
                // 주문 상태 업데이트 로직 종료

                orderService.save(order); // 주문 상태 저장
                return "redirect:/seller/orders";
            } else {
                // 셀러가 그 주문에 접근할 권한이 없음
                return "error/404";
            }
        } else {
            // 존재하지 않는 주문
            return "error/400";
        }
    }

}
