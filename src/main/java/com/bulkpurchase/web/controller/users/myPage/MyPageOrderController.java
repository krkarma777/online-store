package com.bulkpurchase.web.controller.users.myPage;

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
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/myPage")
public class MyPageOrderController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final ProductService productService;
    private final OrderService orderService;
    private final OrderDetailService orderDetailService;
    private final FavoriteProductService favoriteProductService;
    private final PaymentService paymentService;
    private final UserCouponService userCouponService;
    private final UserAuthValidator userAuthValidator;

    @GetMapping("/order/list")
    public String myPageForm(Model model, Principal principal) {
        User user = userAuthValidator.getCurrentUser(principal);
        List<OrderViewDTO> orderViewDTOS = orderService.getOrderViewModelsByUser(user);
        model.addAttribute("orderViewDTOS", orderViewDTOS);
        return "users/myPage/orders";
    }


    @GetMapping("/order/detail/{orderID}")
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
