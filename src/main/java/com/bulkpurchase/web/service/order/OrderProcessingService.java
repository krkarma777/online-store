package com.bulkpurchase.web.service.order;

import com.bulkpurchase.domain.dto.order.OrderFormDataDTO;
import com.bulkpurchase.domain.entity.order.Order;
import com.bulkpurchase.domain.entity.order.OrderDetail;
import com.bulkpurchase.domain.entity.order.Payment;
import com.bulkpurchase.domain.entity.product.Product;
import com.bulkpurchase.domain.service.order.OrderDetailService;
import com.bulkpurchase.domain.service.order.PaymentService;
import com.bulkpurchase.domain.service.product.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class OrderProcessingService {

    private final ProductService productService;
    private final OrderDetailService orderDetailService;
    private final PaymentService paymentService;

    public List<OrderDetail> processOrderDetails(Map<Long, Integer> productIdQuantityMap, Order order) {
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

    public Payment processPayment(Double totalPrice, String paymentMethod, Order order) {
        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setPaymentDate(new Date());
        payment.setAmount(totalPrice);
        payment.setPaymentMethod(paymentMethod);
        if (paymentMethod.equals("Credit Card") || paymentMethod.equals("Kakao Pay")) {
            payment.setStatus("결제 완료");
        } else if (paymentMethod.equals("Bank Transfer")) {
            payment.setStatus("보류");
        }
        return paymentService.save(payment);
    }

    public OrderFormDataDTO extractFormData(HttpServletRequest request) {
        Map<String, String[]> paramMap = request.getParameterMap();
        Map<Long, Integer> productQuantities = new HashMap<>();
        double totalPrice = 0;
        String paymentMethod = "";

        for (Map.Entry<String, String[]> entry : paramMap.entrySet()) {
            String key = entry.getKey();
            String[] value = entry.getValue();
            if (key.startsWith("quantity")) {
                Long productId = Long.parseLong(request.getParameter("product" + key.substring(8)));
                productQuantities.put(productId, Integer.parseInt(value[0]));
            } else if ("totalPrice".equals(key)) {
                totalPrice = Double.parseDouble(value[0]);
            } else if ("paymentMethod".equals(key)) {
                paymentMethod = value[0];
            }
        }
        return new OrderFormDataDTO(productQuantities, totalPrice, paymentMethod);
    }
}
