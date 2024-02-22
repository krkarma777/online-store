package com.bulkpurchase.web.controller.admin;

import com.bulkpurchase.domain.dto.SalesDataDTO;
import com.bulkpurchase.domain.entity.order.Order;
import com.bulkpurchase.domain.entity.product.Category;
import com.bulkpurchase.domain.entity.product.Product;
import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.service.AdminDashboardService;
import com.bulkpurchase.domain.service.order.OrderService;
import com.bulkpurchase.domain.service.product.CategoryService;
import com.bulkpurchase.domain.service.product.ProductService;
import com.bulkpurchase.domain.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    private final CategoryService categoryService;
    private final OrderService orderService;

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

        // 모델에 데이터 추가
        model.addAttribute("last30DaysSales", last30DaysSales);
        model.addAttribute("last12MonthsSales", last12MonthsSales);
        model.addAttribute("last3YearsSales", last3YearsSales);
        return "/admin/statistics";
    }

    // 카테고리 관리 페이지를 조회하는 메서드
    @GetMapping("/category")
    public String categoryManagementPage(Model model) {
        // 모든 카테고리를 계층 구조로 가져오기
        List<Category> categories = categoryService.findAllWithChildren();
        model.addAttribute("categories", categories);
        model.addAttribute("currentCategory", new Category()); // 새 카테고리 추가를 위한 빈 객체
        return "admin/categoryManagement"; // 카테고리 관리 페이지 뷰 이름
    }

    // 카테고리를 저장(추가 또는 수정)하는 메서드
    @PostMapping("/category/save")
    public String saveCategory(@ModelAttribute("currentCategory") Category category,
                               @RequestParam(value = "parent", required = false) Long parentId) {
        if (parentId != null) {
            categoryService.findById(parentId).ifPresent(category::setParent);
        } else {
            category.setParent(null); // 최상위 카테고리로 설정
        }

        categoryService.save(category); // 카테고리 저장
        return "redirect:/admin/category"; // 카테고리 관리 페이지로 리다이렉트
    }
    @GetMapping("/user")
    public String userManagementPage(Model model) {
        List<User> users = userService.findAll();
        model.addAttribute("users", users);

        return "/admin/userManagement";
    }

    @GetMapping("/product")
    public String productManagementPage(Model model) {
        List<Product> products = productService.findAllProducts();
        model.addAttribute("products", products);

        return "/admin/productManagement";
    }

    @GetMapping("/order")
    public String orderManagementPage(Model model) {
        List<Order> orders = orderService.findAll();
        model.addAttribute("orders", orders);

        return "/admin/orderManagement";
    }


}
