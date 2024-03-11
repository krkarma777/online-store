package com.bulkpurchase.web.controller.seller;

import com.bulkpurchase.domain.dto.orderdetail.OrderDetailNameAndIdDTO;
import com.bulkpurchase.domain.entity.product.Product;
import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.service.order.OrderDetailService;
import com.bulkpurchase.domain.service.order.OrderService;
import com.bulkpurchase.domain.service.product.ProductService;
import com.bulkpurchase.domain.validator.user.UserAuthValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;
@Controller
@RequiredArgsConstructor
public class SellerPageController {

    private final ProductService productService;
    private final OrderDetailService orderDetailService;
    private final OrderService orderService;
    private final UserAuthValidator userAuthValidator;

    @GetMapping("/seller")
    public String sellerPageForm(Model model, Principal principal) {
        User user = userAuthValidator.getCurrentUser(principal);
        model.addAttribute("user", user);

        List<Product> productsList = productService.findByUserOrderByProductIDDesc(user);
        model.addAttribute("products", productsList);

        List<OrderDetailNameAndIdDTO> orderDetailList = orderDetailService.findTop5RecentOrderDetailsByUser(user);
        model.addAttribute("orderDetailList", orderDetailList);

        BigDecimal dailySales = orderService.calculateDailySalesBySeller(user.getUserID());
        model.addAttribute("dailySales", dailySales);

        return "seller/sellerPage";
    }

    @GetMapping("/seller/profile/edit")
    public String sellerProfile(Principal principal, Model model) {
        User user = userAuthValidator.getCurrentUser(principal);
        model.addAttribute("user", user);
        return "seller/seller-mypage/user_edit";
    }
}
