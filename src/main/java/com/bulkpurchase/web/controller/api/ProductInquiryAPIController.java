package com.bulkpurchase.web.controller.api;

import com.bulkpurchase.domain.dto.productInquiry.ProductInquiryCreateRequestDTO;
import com.bulkpurchase.domain.entity.product.Product;
import com.bulkpurchase.domain.entity.product.ProductInquiry;
import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.service.product.ProductInquiryService;
import com.bulkpurchase.domain.service.product.ProductService;
import com.bulkpurchase.domain.validator.user.UserAuthValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/product-inquiry")
public class ProductInquiryAPIController {

    private final UserAuthValidator userAuthValidator;
    private final ProductInquiryService productInquiryService;
    private final ProductService productService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody @Validated ProductInquiryCreateRequestDTO requestDTO, Principal principal) {
        User user = userAuthValidator.getCurrentUser(principal);

        Product product = productService.findById(requestDTO.getProductID())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "상품이 존재하지 않습니다."));

        Long inquiryID = productInquiryService.save(new ProductInquiry(requestDTO, product, user)).getInquiryID();

        return ResponseEntity.ok(Map.of("message", "문의가 정상적으로 등록되었습니다.", "inquiryID", inquiryID));
    }
}
