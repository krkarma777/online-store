package com.bulkpurchase.web.controller.order;

import com.bulkpurchase.domain.entity.product.Product;
import com.bulkpurchase.domain.service.ProductService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Controller("/order")
@RequiredArgsConstructor
public class OrderController {
    private final ProductService productService;

/*    @GetMapping("/{productId}")
    public String orderForm(@PathVariable(value = "productID") Long productID, Model model, HttpServletResponse response) {
        Optional<Product> byId = productService.findById(productID);
        if (byId.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400 에러 설정
            return "error/400";
        } else {
            Product product = byId.get();
            model.addAttribute("product", product);
        }
        return "order/orderForm";
    }*/
}
