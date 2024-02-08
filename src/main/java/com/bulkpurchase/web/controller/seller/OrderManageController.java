package com.bulkpurchase.web.controller.seller;

import com.bulkpurchase.domain.dto.UpdateOrderStatusModel;
import com.bulkpurchase.domain.entity.Order;
import com.bulkpurchase.domain.entity.OrderDetail;
import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.enums.OrderStatus;
import com.bulkpurchase.domain.service.order.OrderDetailService;
import com.bulkpurchase.domain.service.order.OrderService;
import com.bulkpurchase.domain.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
                Order order = orderDetail.getOrder();
                order.setStatus(status);
                orderService.save(order);
                return "redirect:/seller/orders";
            } else {
                // 셀러가 그 주문에 접근할 권한이 없음
                return "error/404";
            }
        } else {
            //존재하지 않는 주문
            return "error/400";
        }
    }
}
