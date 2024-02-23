package com.bulkpurchase.web.controller.seller;

import com.bulkpurchase.domain.entity.order.OrderDetail;
import com.bulkpurchase.domain.entity.product.Product;
import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.enums.OrderStatus;
import com.bulkpurchase.domain.service.order.OrderDetailService;
import com.bulkpurchase.domain.service.order.OrderService;
import com.bulkpurchase.domain.service.order.PaymentService;
import com.bulkpurchase.domain.service.product.ProductService;
import com.bulkpurchase.domain.service.user.UserService;
import com.bulkpurchase.web.service.OrderStatusUpdateService;
import com.bulkpurchase.web.validator.user.UserAuthValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/seller/orders")
@RequiredArgsConstructor
public class OrderManageController {

    private final OrderDetailService orderDetailService;
    private final ProductService productService;
    private final OrderStatusUpdateService orderStatusUpdateService;
    private final PaymentService paymentService;
    private final UserAuthValidator userAuthValidator;

    @GetMapping
    public String manageOrders(Principal principal, Model model) {
        User user = userAuthValidator.getCurrentUser(principal);
        List<Product> productsList = productService.findByUserOrderByProductIDDesc(user);
        model.addAttribute("products", productsList);

        List<OrderDetail> orderDetailList = productsList.stream()
                .flatMap(product -> orderDetailService.findByProductOrderByOrderDetailIDDesc(product).stream())
                .sorted(Comparator.comparing(OrderDetail::getOrderDetailID).reversed())
                .collect(Collectors.toList());
        model.addAttribute("orderDetailList", orderDetailList);

        return "seller/orderManage/orders";
    }

    @PostMapping("/edit/{orderDetailID}")
    public String orderStatusChange(@PathVariable("orderDetailID") Long orderDetailID, Principal principal, @RequestParam("orderStatus") OrderStatus status) {
        User user = userAuthValidator.getCurrentUser(principal);
        OrderDetail orderDetail = orderDetailService.findByID(orderDetailID)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "OrderDetail not found"));

        userAuthValidator.validateUserAccessOrderDetail(user, orderDetail);

        orderStatusUpdateService.updateOrderStatus(orderDetail, status);

        return "redirect:/seller/orders";
    }

    @GetMapping("/detail/{orderDetailID}")
    public String orderDetailInfoForSeller(@PathVariable("orderDetailID") Long orderDetailID, Model model) {
        OrderDetail orderDetail = orderDetailService.findByID(orderDetailID)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "OrderDetail not found"));

        model.addAttribute("orderDetail", orderDetail);
        model.addAttribute("order", orderDetail.getOrder());
        model.addAttribute("payment", paymentService.findByOrder(orderDetail.getOrder()));
        return "seller/orderManage/orderDetailForSeller";
    }


}
