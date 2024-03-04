package com.bulkpurchase.web.controller.users;

import com.bulkpurchase.domain.dto.order.OrderViewDTO;
import com.bulkpurchase.domain.entity.coupon.UserCoupon;
import com.bulkpurchase.domain.entity.order.Order;
import com.bulkpurchase.domain.entity.order.OrderDetail;
import com.bulkpurchase.domain.entity.order.Payment;
import com.bulkpurchase.domain.entity.product.Product;
import com.bulkpurchase.domain.entity.user.FavoriteProduct;
import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.service.coupon.UserCouponService;
import com.bulkpurchase.domain.service.order.OrderDetailService;
import com.bulkpurchase.domain.service.order.OrderService;
import com.bulkpurchase.domain.service.order.PaymentService;
import com.bulkpurchase.domain.service.product.ProductService;
import com.bulkpurchase.domain.service.user.FavoriteProductService;
import com.bulkpurchase.domain.service.user.UserService;
import com.bulkpurchase.domain.validator.user.UserAuthValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class MyPageController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final ProductService productService;
    private final OrderService orderService;
    private final OrderDetailService orderDetailService;
    private final FavoriteProductService favoriteProductService;
    private final PaymentService paymentService;
    private final UserCouponService userCouponService;
    private final UserAuthValidator userAuthValidator;

    @GetMapping("/mypage")
    public String myPageForm(Model model, Principal principal) {
        User user = userAuthValidator.getCurrentUser(principal);

        if (user == null) return "error/403";

        List<Product> productsList = productService.findByUserOrderByProductIDDesc(user);

        model.addAttribute("products", productsList);

        model.addAttribute("user", user);

        List<OrderViewDTO> orders = orderService.getOrderViewModelsByUser(user);
        model.addAttribute("orders", orders);

        List<FavoriteProduct> favoriteProducts = favoriteProductService.findByUser(user);
        model.addAttribute("favoriteProducts", favoriteProducts);

        List<UserCoupon> userCoupons = userCouponService.findByUser(user);
        model.addAttribute("userCoupons", userCoupons);

        return "users/myPage";
    }


    @GetMapping("/mypage/edit")
    public String userEditForm(Model model, Principal principal) {
        User user = userAuthValidator.getCurrentUser(principal);
        model.addAttribute("user", user);
        return "users/edit";
    }


    @PostMapping("/mypage/update")
    public String userEditForm(@ModelAttribute User user, Principal principal) {
        log.info("user = {}", user);

        String password = user.getPassword();
        User existingUser = userAuthValidator.getCurrentUser(principal);

        // 비밀번호가 비어 있지 않으면 해시 처리
        if (password != null && !password.isEmpty()) {
            String hashedPassword = passwordEncoder.encode(password);
            user.setPassword(hashedPassword);
        } else {
            // 기존 비밀번호 유지
            user.setPassword(existingUser.getPassword());
        }


        userService.save(user);
        return "redirect:/mypage";
    }

    @GetMapping("/orders/detail/{orderID}")
    public String orderDetailInfoForSeller(@PathVariable("orderID") Long orderID, Model model) {
        Order order = orderService.findById(orderID).orElse(null);
        Payment payment = paymentService.findByOrder(order);
        List<OrderDetail> orderDetailList = orderDetailService.findByOrder(order);

        model.addAttribute("orderDetailList", orderDetailList);
        model.addAttribute("order", order);
        model.addAttribute("payment", payment);
        return "/users/orderDetailForUser";
    }
}
