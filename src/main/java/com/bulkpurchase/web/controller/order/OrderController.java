package com.bulkpurchase.web.controller.order;

import com.bulkpurchase.domain.dto.order.OrderViewDTO;
import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.service.order.OrderService;
import com.bulkpurchase.domain.validator.user.UserAuthValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final UserAuthValidator userAuthValidator;

    @GetMapping("list")
    public String orderList(Model model, Principal principal) {
        try {
            User user = userAuthValidator.getCurrentUser(principal);
            List<OrderViewDTO> orderViewDTOS = orderService.getOrderViewModelsByUser(user);
            model.addAttribute("orderViewDTOS", orderViewDTOS);
            return "users/orders";
        } catch (Exception e) {
            return "error/400";
        }
    }
}
