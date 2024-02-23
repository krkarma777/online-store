package com.bulkpurchase.web.controller.seller;

import com.bulkpurchase.domain.dto.ReviewDetailDTO;
import com.bulkpurchase.domain.entity.order.OrderDetail;
import com.bulkpurchase.domain.entity.product.Product;
import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.service.order.OrderDetailService;
import com.bulkpurchase.domain.service.order.OrderService;
import com.bulkpurchase.domain.service.order.PaymentService;
import com.bulkpurchase.domain.service.product.ProductService;
import com.bulkpurchase.domain.service.review.ReviewService;
import com.bulkpurchase.domain.service.user.UserService;
import com.bulkpurchase.web.validator.user.UserAuthValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
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

        List<OrderDetail> orderDetailList = aggregateOrderDetails(productsList);
        model.addAttribute("orderDetailList", orderDetailList);

        BigDecimal dailySales = orderService.calculateDailySalesBySeller(user.getUserID());
        model.addAttribute("dailySales", dailySales);

        return "seller/sellerPage";
    }

    private List<OrderDetail> aggregateOrderDetails(List<Product> productsList) {
        List<OrderDetail> orderDetailList = new ArrayList<>();
        for (Product product : productsList) {
            orderDetailList.addAll(orderDetailService.findByProductOrderByOrderDetailIDDesc(product));
        }
        return orderDetailList;
    }
}
