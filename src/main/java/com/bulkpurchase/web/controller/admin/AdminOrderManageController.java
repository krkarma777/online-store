package com.bulkpurchase.web.controller.admin;

import com.bulkpurchase.domain.entity.order.Order;
import com.bulkpurchase.domain.enums.OrderStatus;
import com.bulkpurchase.domain.enums.UserStatus;
import com.bulkpurchase.domain.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminOrderManageController {

    private final OrderService orderService;

    @GetMapping("/order")
    public String orderManagementPage(Model model) {
        List<Order> orders = orderService.findByOrderByOrderIDDesc();
        model.addAttribute("orders", orders);

        return "/admin/orderManagement";
    }

    @PostMapping("/order/status")
    @ResponseBody
    public ResponseEntity<?> updateOrderByAdmin(@RequestParam("orderId") Long orderId, @RequestParam("status")OrderStatus status) {
        Order order = orderService.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "상품을 찾을 수 없습니다."));

        order.setStatus(status);
        orderService.save(order);
        return ResponseEntity.ok("ok");
    }
    @GetMapping("/order/delete/{orderID}")
    public String updateDeleteByAdmin(@PathVariable("orderID") Long orderID) {
        Order order = orderService.findById(orderID)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "상품을 찾을 수 없습니다."));

        order.setStatus(OrderStatus.CANCELLED);
        orderService.save(order);
        return "redirect:/admin/order";
    }


}
