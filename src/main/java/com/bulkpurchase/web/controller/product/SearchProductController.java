package com.bulkpurchase.web.controller.product;

import com.bulkpurchase.domain.dto.ProductForCouponDTO;
import com.bulkpurchase.domain.entity.product.Product;
import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.service.product.ProductService;
import com.bulkpurchase.domain.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class SearchProductController {

    private final ProductService productService;
    private final UserService userService;

    @GetMapping("/searchProduct")
    public ResponseEntity<List<ProductForCouponDTO>> searchProduct(@RequestParam("query") String productName, Principal principal) {
        User user = userService.findByUsername(principal.getName()).orElse(null);
        if (user == null) {
            return ResponseEntity.status(403).body(null);
        }
        List<ProductForCouponDTO> productList = productService.findByProductNameContaining(productName, user);

        // 상품 목록을 JSON 형태로 클라이언트에 반환
        return ResponseEntity.ok(productList);
    }
}
