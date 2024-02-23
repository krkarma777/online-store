package com.bulkpurchase.web.controller.admin;

import com.bulkpurchase.domain.entity.order.Order;
import com.bulkpurchase.domain.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminOrderManageController {

    private final OrderService orderService;

    @GetMapping("/order")
    public String orderManagementPage(Model model) {
        List<Order> orders = orderService.findAll();
        model.addAttribute("orders", orders);

        return "/admin/orderManagement";
    }
}
