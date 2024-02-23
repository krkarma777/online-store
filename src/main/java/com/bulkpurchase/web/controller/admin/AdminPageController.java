package com.bulkpurchase.web.controller.admin;

import com.bulkpurchase.domain.entity.product.Product;
import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.service.AdminDashboardService;
import com.bulkpurchase.domain.service.order.OrderService;
import com.bulkpurchase.domain.service.product.ProductService;
import com.bulkpurchase.domain.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminPageController {

    private final ProductService productService;
    private final UserService userService;
    private final AdminDashboardService adminDashboardService;

    @GetMapping
    public String adminPageForm(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName()).orElse(null);

        List<Product> productsList = productService.findAllProducts();
        model.addAttribute("products", productsList);

        model.addAttribute("totalUsers", adminDashboardService.getTotalUsers());
        model.addAttribute("totalProducts", adminDashboardService.getTotalProducts());
        model.addAttribute("totalInProgressOrders", adminDashboardService.getTotalOrdersExcludingDeliveredAndCancelled());

        model.addAttribute("user", user);
        return "admin/adminPage";
    }
}
