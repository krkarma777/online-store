package com.bulkpurchase.web.controller.api;

import com.bulkpurchase.domain.dto.productInquiry.ProductInquiryCreateRequestDTO;
import com.bulkpurchase.domain.dto.productInquiry.ProductInquiryUpdateRequestDTO;
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
import org.springframework.web.bind.annotation.*;
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

    @PatchMapping
    public ResponseEntity<?> update(@RequestBody ProductInquiryUpdateRequestDTO requestDTO,
                                    Principal principal) {

        if (requestDTO.getInquiryContent() == null || requestDTO.getInquiryID() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 요청입니다.");
        }

        ProductInquiry productInquiry = productInquiryService.findById(requestDTO.getInquiryID())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "문의가 존재하지 않습니다."));

        User user = userAuthValidator.getCurrentUser(principal);

        if (!productInquiry.getUser().equals(user)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "글을 수정할 권한이 없습니다.");
        }

        if (productInquiry.getReplyDate() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "답변이 달린 글은 수정할 수 없습니다.");
        }

        productInquiry.update(requestDTO);
        productInquiryService.save(productInquiry);

        return ResponseEntity.ok(Map.of("message", "문의 수정이 완료되었습니다.", "inquiryID", productInquiry.getInquiryID()));
    }
}
