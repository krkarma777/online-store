package com.bulkpurchase.web.controller.seller;

import com.bulkpurchase.domain.dto.UpdateOrderStatusModel;
import com.bulkpurchase.domain.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping
@RequiredArgsConstructor
public class OrderManageController {

    private final OrderService orderService;

    @PostMapping("/orders/edit/{orderID}")
    public String orderStatusChange(@PathVariable(value = "orderID") Long orderID) {

        return "redirect:/seller/orders";
    }

}
