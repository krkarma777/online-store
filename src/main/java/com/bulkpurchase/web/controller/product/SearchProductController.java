package com.bulkpurchase.web.controller.product;

import com.bulkpurchase.domain.dto.ProductForCouponDTO;
import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.enums.ErrorPage;
import com.bulkpurchase.domain.service.product.ProductService;
import com.bulkpurchase.domain.service.user.UserService;
import lombok.RequiredArgsConstructor;
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
    public List<ProductForCouponDTO> searchProduct(@RequestParam("query") String productName, Principal principal) {
        User user = userService.findByUsername(principal.getName()).orElse(null);
        if (user == null) {
            return null; // 또는 예외 처리를 수행할 수도 있음
        }
        return productService.findByProductNameContaining(productName, user);
    }
}
