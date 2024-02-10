package com.bulkpurchase.web.controller.admin;

import com.bulkpurchase.domain.dto.SalesDataDTO;
import com.bulkpurchase.domain.entity.product.Product;
import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.service.AdminDashboardService;
import com.bulkpurchase.domain.service.ProductService;
import com.bulkpurchase.domain.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
@Slf4j
public class AdminPageController {

    private final ProductService productService;
    private final UserService userService;
    private final AdminDashboardService adminDashboardService;

    @GetMapping
    public String adminPageForm(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        if (!user.getRole().equals("ROLE_관리자")) {
            return "error/403";
        }
        List<Product> productsList = productService.findAllProducts();
        model.addAttribute("products", productsList);

        model.addAttribute("totalUsers", adminDashboardService.getTotalUsers());
        model.addAttribute("totalProducts", adminDashboardService.getTotalProducts());
        model.addAttribute("totalInProgressOrders", adminDashboardService.getTotalOrdersExcludingDeliveredAndCancelled());

        model.addAttribute("user", user);
        return "admin/adminPage";
    }

    @GetMapping("/statistics")
    public String statisticsPage(Model model) {

        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(30);

        // 최근 30일 판매 데이터
        List<SalesDataDTO> last30DaysSales = adminDashboardService.calculateSalesLast30Days(startDate, endDate);
        // 지난 12개월 판매 데이터
        List<SalesDataDTO> last12MonthsSales = adminDashboardService.calculateSalesLast12Months();
        // 최근 3년 판매 데이터
        List<SalesDataDTO> last3YearsSales = adminDashboardService.calculateSalesLast3Years();

        log.info("last30DaysSales = {}", last30DaysSales);
        log.info("last12MonthsSales = {}", last12MonthsSales);
        log.info("last3YearsSales = {}", last3YearsSales);
        // 모델에 데이터 추가
        model.addAttribute("last30DaysSales", last30DaysSales);
        model.addAttribute("last12MonthsSales", last12MonthsSales);
        model.addAttribute("last3YearsSales", last3YearsSales);
        return "/admin/statistics";
    }

    @GetMapping("/category")
    public String categoryManagementPage() {
        return "/admin/categoryManagement";
    }

    @GetMapping("/user")
    public String userManagementPage() {
        return "/admin/userManagement";
    }

    @GetMapping("/product")
    public String productManagementPage() {
        return "/admin/productManagement";
    }

    @GetMapping("/order")
    public String orderManagementPage() {
        return "/admin/orderManagement";
    }


}
