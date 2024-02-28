package com.bulkpurchase.web.service.product;

import com.bulkpurchase.domain.enums.ProductStatus;
import com.bulkpurchase.domain.service.product.ProductService;
import com.bulkpurchase.domain.validator.user.UserAuthValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
@RequiredArgsConstructor
public class ProductStatusService {

    private final ProductService productService;
    private final UserAuthValidator userAuthValidator;
    public String updateProductStatus(Long productID, Principal principal, ProductStatus status) {
        return productService.findById(productID)
                .filter(product -> userAuthValidator.isProductOwner(principal, product))
                .map(product -> {
                    product.setStatus(status);
                    productService.saveProduct(product);
                    return "redirect:/seller/products";
                })
                .orElse("error/403");
    }
}
