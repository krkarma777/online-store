package com.bulkpurchase.web.controller.seller;

import com.bulkpurchase.domain.entity.OrderDetail;
import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.entity.product.Product;
import com.bulkpurchase.domain.enums.ProductStatus;
import com.bulkpurchase.domain.service.ProductService;
import com.bulkpurchase.domain.service.order.OrderDetailService;
import com.bulkpurchase.domain.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/seller")
@RequiredArgsConstructor
public class SellerPageController {

    private final ProductService productService;
    private final UserService userService;
    private final OrderDetailService orderDetailService;


    @GetMapping("/products")
    public String manageProducts(Principal principal, Model model) {

        User user = userService.findByUsername(principal.getName());
        List<Product> products = productService.findByUserOrderByProductIDDesc(user);
        model.addAttribute("products", products);

        for (Product product : products) {
            ProductStatus status = product.getStatus();
            System.out.println("status = " + status);
        }
        return "/seller/products";
    }
    @GetMapping("/orders")
    public String manageOrders(Principal principal, Model model) {

        User user = userService.findByUsername(principal.getName());
        List<Product> productsList = productService.findByUserOrderByProductIDDesc(user);
        model.addAttribute("products", productsList);

        List<OrderDetail> orderDetailList = new ArrayList<>();
        for (Product product : productsList) {
            Optional<OrderDetail> orderDetailOpt = orderDetailService.findByProductOrderByOrderDetailIDDesc(product);
            orderDetailOpt.ifPresent(orderDetailList::add);
        }
        model.addAttribute("orderDetailList", orderDetailList);

        return "/seller/orders";
    }

}
