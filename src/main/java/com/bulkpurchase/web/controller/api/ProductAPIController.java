package com.bulkpurchase.web.controller.api;

import com.bulkpurchase.domain.dto.product.CreateProductDTO;
import com.bulkpurchase.domain.entity.product.Product;
import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.service.product.ProductService;
import com.bulkpurchase.domain.validator.user.UserAuthValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/product")
public class ProductAPIController {

    private final UserAuthValidator userAuthValidator;
    private final ProductService productService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody @Validated CreateProductDTO createProductDTO,
                                    BindingResult bindingResult,
                                    Principal principal) {

        if (bindingResult.hasErrors()) {
            Map<String, String> errors = bindingResult.getFieldErrors().stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "입력 값에 오류가 있습니다.", "errors", errors));
        }

        User user = userAuthValidator.getCurrentUser(principal);
        Product product = new Product(createProductDTO, user);
        Long productID = productService.save(product).getProductID();

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "상품 등록에 성공하셨습니다.", "productID", productID));
    }
}
