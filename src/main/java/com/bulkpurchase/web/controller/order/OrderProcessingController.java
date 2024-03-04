package com.bulkpurchase.web.controller.order;


import com.bulkpurchase.domain.dto.order.OrderFormDataDTO;
import com.bulkpurchase.domain.entity.order.Order;
import com.bulkpurchase.domain.entity.order.OrderDetail;
import com.bulkpurchase.domain.entity.order.Payment;
import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.service.order.OrderService;
import com.bulkpurchase.domain.validator.user.UserAuthValidator;
import com.bulkpurchase.web.service.order.OrderProcessingService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderProcessingController {

    private final OrderService orderService;
    private final UserAuthValidator userAuthValidator;
    private final OrderProcessingService orderProcessingService;

    @PostMapping
    public String orderProcess(HttpServletRequest request, Principal principal, Model model) {
        User user = userAuthValidator.getCurrentUser(principal);
        OrderFormDataDTO formData = orderProcessingService.extractFormData(request);
        Order order = orderService.saveOrder(user, formData.getTotalPrice());
        List<OrderDetail> orderDetails = orderProcessingService.processOrderDetails(formData.getProductQuantities(), order);
        Payment savedPayment = orderProcessingService.processPayment(formData.getTotalPrice(), formData.getPaymentMethod(), order);

        model.addAttribute("order", order);
        model.addAttribute("orderDetails", orderDetails);
        model.addAttribute("savedPayment", savedPayment);

        return "order/orderSuccess";
    }
}

