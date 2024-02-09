package com.bulkpurchase.web.controller.seller;

import com.bulkpurchase.domain.entity.OrderDetail;
import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.entity.product.Product;
import com.bulkpurchase.domain.enums.ProductStatus;
import com.bulkpurchase.domain.service.ProductService;
import com.bulkpurchase.domain.service.order.OrderDetailService;
import com.bulkpurchase.domain.service.order.OrderService;
import com.bulkpurchase.domain.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/seller")
@RequiredArgsConstructor
@Slf4j
public class SellerPageController {

    private final ProductService productService;
    private final UserService userService;
    private final OrderDetailService orderDetailService;
    private final OrderService orderService;

    @GetMapping
    public String sellerPageForm(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        if (!user.getRole().equals("ROLE_판매자")) {
            return "error/403";
        }
        model.addAttribute("user", user);

        List<Product> productsList = productService.findByUserOrderByProductIDDesc(user);
        model.addAttribute("products", productsList);

        List<OrderDetail> orderDetailList = new ArrayList<>();
        for (Product product : productsList) {
            orderDetailList = orderDetailService.findByProductOrderByOrderDetailIDDesc(product);
        }
        model.addAttribute("orderDetailList", orderDetailList);

        BigDecimal dailySales = orderService.calculateDailySalesBySeller(user.getUserID());
        model.addAttribute("dailySales", dailySales);
        log.info("dailySales = {}", dailySales);

        return "seller/sellerPage";
    }
    @GetMapping("/products")
    public String manageProducts(Principal principal, Model model) {

        User user = userService.findByUsername(principal.getName());
        List<Product> products = productService.findByUserOrderByProductIDDesc(user);
        model.addAttribute("products", products);

        for (Product product : products) {
            ProductStatus status = product.getStatus();
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
            orderDetailList = orderDetailService.findByProductOrderByOrderDetailIDDesc(product);
        }
        model.addAttribute("orderDetailList", orderDetailList);

        return "/seller/orders";
    }

    @GetMapping("/sales")
    public String salesView(Model model, Principal principal) {
        Long userID = userService.findByUsername(principal.getName()).getUserID();

        LocalDate today = LocalDate.now();
        int year = today.getYear();
        int month = today.getMonthValue();

        BigDecimal totalSales = orderService.calculateTotalSalesBySeller(userID);
        BigDecimal yearlySales = orderService.calculateYearlySalesBySeller(year, userID);
        BigDecimal monthlySales = orderService.calculateMonthlySalesBySeller(year, month, userID);
        BigDecimal dailySales = orderService.calculateDailySalesBySeller(userID);

        model.addAttribute("totalSales", totalSales);
        model.addAttribute("yearlySales", yearlySales);
        model.addAttribute("monthlySales", monthlySales);
        model.addAttribute("dailySales", dailySales);

        String currentDate = today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        model.addAttribute("currentDate", currentDate);

        return "/seller/sales";
    }


}
