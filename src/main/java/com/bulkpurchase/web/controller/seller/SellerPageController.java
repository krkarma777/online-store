package com.bulkpurchase.web.controller.seller;

import com.bulkpurchase.domain.dto.ReviewDetailDTO;
import com.bulkpurchase.domain.dto.SalesDataDTO;
import com.bulkpurchase.domain.entity.order.Order;
import com.bulkpurchase.domain.entity.order.OrderDetail;
import com.bulkpurchase.domain.entity.order.Payment;
import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.entity.product.Product;
import com.bulkpurchase.domain.enums.ProductStatus;
import com.bulkpurchase.domain.service.order.PaymentService;
import com.bulkpurchase.domain.service.product.ProductInquiryService;
import com.bulkpurchase.domain.service.product.ProductService;
import com.bulkpurchase.domain.service.order.OrderDetailService;
import com.bulkpurchase.domain.service.order.OrderService;
import com.bulkpurchase.domain.service.review.ReviewService;
import com.bulkpurchase.domain.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/seller")
@RequiredArgsConstructor
@Slf4j
public class SellerPageController {

    private final ProductService productService;
    private final UserService userService;
    private final OrderDetailService orderDetailService;
    private final OrderService orderService;
    private final ReviewService reviewService;
    private final PaymentService paymentService;

    @GetMapping
    public String sellerPageForm(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName()).orElse(null);
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

        return "seller/sellerPage";
    }

    @GetMapping("/products")
    public String manageProducts(Principal principal, Model model) {

        User user = userService.findByUsername(principal.getName()).orElse(null);
        List<Product> products = productService.findByUserOrderByProductIDDesc(user);
        model.addAttribute("products", products);

        for (Product product : products) {
            ProductStatus status = product.getStatus();
        }
        return "/seller/productManage/products";
    }

    @GetMapping("/orders")
    public String manageOrders(Principal principal, Model model) {

        User user = userService.findByUsername(principal.getName()).orElse(null);
        List<Product> productsList = productService.findByUserOrderByProductIDDesc(user);
        model.addAttribute("products", productsList);

        List<OrderDetail> orderDetailList = new ArrayList<>();
        for (Product product : productsList) {
            List<OrderDetail> orderDetailIDDesc = orderDetailService.findByProductOrderByOrderDetailIDDesc(product);
            orderDetailList.addAll(orderDetailIDDesc);
        }
        orderDetailList.sort(Comparator.comparing(OrderDetail::getOrderDetailID).reversed());
        model.addAttribute("orderDetailList", orderDetailList);

        return "/seller/orderManage/orders";
    }

    @GetMapping("/sales")
    public String salesView(Model model, Principal principal) {
        Long userID = userService.findByUsername(principal.getName()).orElse(null).getUserID();

        LocalDate endDate = LocalDate.now().plusDays(1);
        LocalDate startDate = endDate.minusDays(30);

        // 최근 30일 판매 데이터
        List<SalesDataDTO> last30DaysSales = orderService.calculateSalesLast30DaysBySeller(userID, startDate, endDate);
        // 날짜순으로 정렬
        last30DaysSales.sort(Comparator.comparing(SalesDataDTO::getPeriod));
        // 지난 12개월 판매 데이터
        List<SalesDataDTO> last12MonthsSales = orderService.calculateSalesLast12MonthsBySeller(userID);
        // 최근 3년 판매 데이터
        List<SalesDataDTO> last3YearsSales = orderService.calculateSalesLast3YearsBySeller(userID);

        // 모델에 데이터 추가
        model.addAttribute("last30DaysSales", last30DaysSales);
        model.addAttribute("last12MonthsSales", last12MonthsSales);
        model.addAttribute("last3YearsSales", last3YearsSales);

        return "/seller/sales";
    }

    @GetMapping("/reviews")
    public String reviewManage(Principal principal, Model model) {
        User user = userService.findByUsername(principal.getName()).orElse(null);
        List<ReviewDetailDTO> reviews = reviewService.findAllReviewDetailsWithFeedbackCountsBySeller(user.getUserID());
        model.addAttribute("reviews", reviews);
        return "/seller/productManage/reviews";
    }

    @GetMapping("/orders/detail/{orderDetailID}")
    public String orderDetailInfoForSeller(@PathVariable("orderDetailID") Long orderDetailID, Model model) {
        OrderDetail orderDetail = orderDetailService.findByID(orderDetailID).orElse(null);
        if (orderDetail == null) {
            return "error/403";
        }
        Order order = orderDetail.getOrder();
        Payment payment = paymentService.findByOrder(order);

        model.addAttribute("orderDetail", orderDetail);
        model.addAttribute("order", order);
        model.addAttribute("payment", payment);
        return "/seller/orderManage/orderDetailForSeller";
    }


}
