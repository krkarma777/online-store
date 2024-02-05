package com.bulkpurchase.web.controller.order;

import com.bulkpurchase.domain.entity.*;
import com.bulkpurchase.domain.entity.product.Product;
import com.bulkpurchase.domain.service.ProductService;
import com.bulkpurchase.domain.service.cart.CartService;
import com.bulkpurchase.domain.service.order.OrderDetailService;
import com.bulkpurchase.domain.service.order.OrderService;
import com.bulkpurchase.domain.service.order.PaymentService;
import com.bulkpurchase.domain.service.user.UserService;
import com.paypal.base.rest.PayPalRESTException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.*;

@Controller
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {
    private final ProductService productService;
    private final CartService cartService;
    private final OrderService orderService;
    private final UserService userService;
    private final OrderDetailService orderDetailService;
    private final PaymentService paymentService;

    @GetMapping("/{cartID}")
    public String orderForm(@PathVariable(value = "cartID") Long cartID, Model model, HttpServletResponse response) {
        Optional<Cart> cartOpt = cartService.findById(cartID);
        if (cartOpt.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400 에러 설정
            return "error/400";
        }

        Cart cart = cartOpt.get();
        Set<CartItem> items = cart.getItems();

        model.addAttribute("cartID", cartID);
        System.out.println("cartID = " + cartID);
        model.addAttribute("items", items);
        return "order/orderForm";
    }

    @PostMapping
    public String orderProcess(HttpServletRequest request,
                               @RequestParam("totalPrice") Double totalPrice,
                               @RequestParam("paymentMethod") String paymentMethod,
                               @RequestParam("cartID") Long cartID,
                               Principal principal, Model model) {

        System.out.println("totalPrice = " + totalPrice);
        User user = userService.findByUsername(principal.getName());
        Map<Long, Integer> productIdQuantityMap = new HashMap<>();
        Order order = orderService.saveOrder(user, totalPrice);
        model.addAttribute("order", order);

        // 제품 ID와 수량 파싱
        Enumeration<String> params = request.getParameterNames();
        while (params.hasMoreElements()) {
            String paramName = params.nextElement();
            if (paramName.startsWith("product_")) {
                Long productId = Long.valueOf(paramName.split("_")[1]);
                Integer quantity = Integer.valueOf(request.getParameter(paramName));
                productIdQuantityMap.put(productId, quantity);
            }
        }

        // 주문 항목 처리
        List<OrderDetail> orderDetails = new ArrayList<>();
        for (Map.Entry<Long, Integer> entry : productIdQuantityMap.entrySet()) {
            Long productId = entry.getKey();
            Optional<Product> productOpt = productService.findById(productId);
            if (productOpt.isPresent()) {
                Product product = productOpt.get();
                Integer quantity = entry.getValue();
                OrderDetail orderDetail = orderDetailService.save(order, product, quantity);
                orderDetails.add(orderDetail);
            }
        }
        model.addAttribute("orderDetails", orderDetails);

        // 결제 정보 생성
        Payment payment = new Payment();
        payment.setOrder(order); // 주문 정보 설정
        payment.setPaymentDate(new Date()); // 현재 날짜로 결제일 설정
        payment.setAmount(totalPrice); // 폼에서 받은 총 가격 설정
        payment.setPaymentMethod(paymentMethod); // 폼에서 받은 결제 방식 설정

        // 결제 정보 저장
        Payment savedPayment = paymentService.save(payment);

        model.addAttribute("savedPayment", savedPayment);

        cartService.deleteById(cartID);

        return "order/orderSuccess";
    }


}
