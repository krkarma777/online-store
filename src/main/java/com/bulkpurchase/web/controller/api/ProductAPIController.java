package com.bulkpurchase.web.controller.api;

import com.bulkpurchase.domain.dto.product.ProductRequestDTO;
import com.bulkpurchase.domain.dto.product.ProductResponseDTO;
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
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/product")
public class ProductAPIController {

    private final UserAuthValidator userAuthValidator;
    private final ProductService productService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody @Validated ProductRequestDTO productRequestDTO,
                                    BindingResult bindingResult,
                                    Principal principal) {

        if (bindingResult.hasErrors()) {
            Map<String, String> errors = bindingResult.getFieldErrors().stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "입력 값에 오류가 있습니다.", "errors", errors));
        }

        User user = userAuthValidator.getCurrentUser(principal);
        Product product = new Product(productRequestDTO, user);
        Long productID = productService.save(product).getProductID();

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "상품 등록에 성공하셨습니다.", "productID", productID));
    }

    @PatchMapping("/{productID}")
    public ResponseEntity<?> update(@RequestBody @Validated ProductRequestDTO productRequestDTO,
                                    @PathVariable("productID") Long productID,
                                    Principal principal, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = bindingResult.getFieldErrors().stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "입력 값에 오류가 있습니다.", "errors", errors));
        }

        Optional<Product> productOpt = productService.findById(productID);
        if (productOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "존재하지 않는 상품입니다."));
        }

        User user = userAuthValidator.getCurrentUser(principal);
        Product product = productOpt.get();
        if (!product.getUser().getUserID()
                .equals(user.getUserID())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "상품을 수정할 권한이 없습니다."));
        }

        product.update(productRequestDTO);
        productService.save(product);

        return ResponseEntity.ok(Map.of("message", "상품 수정에 성공하셨습니다."));
    }

    @GetMapping("/{productID}")
    public ResponseEntity<?> readOne(@PathVariable("productID") Long productID) {
        Optional<Product> productOpt = productService.findById(productID);
        if (productOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "존재하지 않는 상품입니다."));
        }

        ProductResponseDTO productResponseDTO = new ProductResponseDTO(productOpt.get());
        return  ResponseEntity.ok(Map.of("product", productResponseDTO));
    }
}
