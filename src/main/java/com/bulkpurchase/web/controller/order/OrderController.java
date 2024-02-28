package com.bulkpurchase.web.controller.order;

import com.bulkpurchase.domain.dto.order.OrderViewDTO;
import com.bulkpurchase.domain.entity.cart.Cart;
import com.bulkpurchase.domain.entity.cart.CartItem;
import com.bulkpurchase.domain.entity.order.Order;
import com.bulkpurchase.domain.entity.order.OrderDetail;
import com.bulkpurchase.domain.entity.order.Payment;
import com.bulkpurchase.domain.entity.product.Product;
import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.service.cart.CartService;
import com.bulkpurchase.domain.service.order.OrderDetailService;
import com.bulkpurchase.domain.service.order.OrderService;
import com.bulkpurchase.domain.service.order.PaymentService;
import com.bulkpurchase.domain.service.product.ProductService;
import com.bulkpurchase.domain.validator.user.UserAuthValidator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.*;

@Controller
@RequestMapping("/order")
@RequiredArgsConstructor
@Slf4j
public class OrderController {
    private final ProductService productService;
    private final CartService cartService;
    private final OrderService orderService;
    private final OrderDetailService orderDetailService;
    private final PaymentService paymentService;
    private final UserAuthValidator userAuthValidator;

    @PostMapping("/item/one")
    public String oneItemOrder(@RequestParam("productID") Long productID,
                               @RequestParam("quantity") Integer quantity,
                               Model model) {
        List<CartItem> buyItems = new ArrayList<>();
        Product product = productService.findById(productID).orElse(null);
        if (product == null) {
            return "error/403";
        }
        Cart cart = new Cart();
        CartItem cartItem = new CartItem(cart, product, quantity);

        buyItems.add(cartItem);
        model.addAttribute("items", buyItems);
        return "order/orderForm";
    }


    @GetMapping("/{cartID}")
    public String orderForm(@PathVariable(value = "cartID") Long cartID, Model model, HttpServletResponse response, @RequestParam("itemId") List<Long> itemIds) {
        Optional<Cart> cartOpt = cartService.findById(cartID);
        if (cartOpt.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400 에러 설정
            return "error/400";
        }
        Cart cart = cartOpt.get();
        Set<CartItem> items = cart.getItems();
        List<CartItem> buyItems = new ArrayList<>();
        Iterator<CartItem> iterator = items.iterator();
        while (iterator.hasNext()) {
            CartItem item = iterator.next();
            for (Long itemId : itemIds) {
                if (item.getCartItemID().equals(itemId)) {
                    buyItems.add(item);
                    iterator.remove();
                }
            }
        }
        cart.setItems(items);
        cartService.save(cart);

        model.addAttribute("items", buyItems);
        return "order/orderForm";
    }

    @PostMapping
    public String orderProcess(HttpServletRequest request,
                               Principal principal, Model model) {
        Map<String, String[]> paramMap = request.getParameterMap();
        Map<Long, Integer> productQuantities = new HashMap<>();
        double totalPrice = 0;
        String paymentMethod = "";

        for (Map.Entry<String, String[]> entry : paramMap.entrySet()) {
            String key = entry.getKey();
            String[] value = entry.getValue();

            if (key.startsWith("quantity")) {
                int index = Integer.parseInt(key.substring("quantity".length()));
                int quantity = Integer.parseInt(value[0]);
                // 여기서 product ID를 찾아서 매핑해야 함
                Long productId = Long.parseLong(request.getParameter("product_" + index));
                productQuantities.put(productId, quantity);
            } else if (key.equals("totalPrice")) {
                totalPrice = Double.parseDouble(value[0]);
            } else if (key.equals("paymentMethod")) {
                paymentMethod = value[0];
            }
        }

        // 주문 생성
        User user = userAuthValidator.getCurrentUser(principal);
        Order order = orderService.saveOrder(user, totalPrice);
        model.addAttribute("order", order);

        // 주문 항목 처리
        List<OrderDetail> orderDetails = getOrderDetails(productQuantities, order);
        model.addAttribute("orderDetails", orderDetails);

        // 결제 정보 생성
        Payment payment = getPayment(totalPrice, paymentMethod, order);
        
        // 결제 정보 저장
        Payment savedPayment = paymentService.save(payment);
        model.addAttribute("savedPayment", savedPayment);

        return "order/orderSuccess";
    }

    @GetMapping("list")
    public String orderList(Model model, Principal principal) {
        try {
            User user = userAuthValidator.getCurrentUser(principal);
            List<OrderViewDTO> orderViewDTOS = orderService.getOrderViewModelsByUser(user);
            model.addAttribute("orderViewDTOS", orderViewDTOS);
            return "order/orders";
        } catch (Exception e) {
            return "error/400";
        }
    }
    private List<OrderDetail> getOrderDetails(Map<Long, Integer> productIdQuantityMap, Order order) {
        List<OrderDetail> orderDetails = new ArrayList<>();
        for (Map.Entry<Long, Integer> entry : productIdQuantityMap.entrySet()) {
            Long productId = entry.getKey();
            Integer quantity = entry.getValue();

            Optional<Product> productOpt = productService.findById(productId);
            if (productOpt.isPresent()) {
                Product product = productOpt.get();
                OrderDetail orderDetail = orderDetailService.save(order, product, quantity);
                orderDetails.add(orderDetail);
            }
        }
        return orderDetails;
    }

    private Payment getPayment(Double totalPrice, String paymentMethod, Order order) {
        Payment payment = new Payment();
        payment.setOrder(order); // 주문 정보 설정
        payment.setPaymentDate(new Date()); // 현재 날짜로 결제일 설정
        payment.setAmount(totalPrice); // 폼에서 받은 총 가격 설정
        payment.setPaymentMethod(paymentMethod); // 폼에서 받은 결제 방식 설정
        if (paymentMethod.equals("Credit Card") || paymentMethod.equals("Kakao Pay")) {
            payment.setStatus("결제 완료");
        } else if (paymentMethod.equals("Bank Transfer")) {
            payment.setStatus("보류");
        }
        return payment;
    }

}
