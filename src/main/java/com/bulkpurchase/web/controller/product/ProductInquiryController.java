package com.bulkpurchase.web.controller.product;

import com.bulkpurchase.domain.entity.product.Product;
import com.bulkpurchase.domain.entity.product.ProductInquiry;
import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.service.product.ProductInquiryService;
import com.bulkpurchase.domain.service.product.ProductService;
import com.bulkpurchase.domain.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class ProductInquiryController {

    private final ProductInquiryService productInquiryService;
    private final UserService userService;
    private final ProductService productService;



    @PostMapping("/product/{productID}/inquiry/add")
    public ResponseEntity<?> productInquiryAdd(@PathVariable("productID") Long productID, Principal principal, @RequestBody ProductInquiry productInquiry) {
        User user = userService.findByUsername(principal.getName());
        Product product = productService.findById(productID).orElse(null);
        if (user == null) {
            return ResponseEntity.badRequest().body("회원가입이 필요한 서비스입니다.");
        }

        if (product == null) {
            return ResponseEntity.badRequest().body("상품이 존재하지 않습니다.");
        }

        productInquiry.setUser(user);
        productInquiry.setProduct(product);
        productInquiryService.save(productInquiry);

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "상품 문의 등록 완료");
        response.put("productID", productID);
        response.put("userName", user.getRealName());
        response.put("inquiryContent", productInquiry.getInquiryContent());

        return ResponseEntity.ok(response);
    }
}
