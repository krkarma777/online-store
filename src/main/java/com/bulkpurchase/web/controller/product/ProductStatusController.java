package com.bulkpurchase.web.controller.product;

import com.bulkpurchase.domain.enums.ProductStatus;
import com.bulkpurchase.web.service.product.ProductStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductStatusController {

    private final ProductStatusService productStatusService;

    @GetMapping("/delete/{productID}")
    public String deleteProduct(@PathVariable(value = "productID") Long productID, Principal principal) {
        return productStatusService.updateProductStatus(productID, principal, ProductStatus.INACTIVE);
    }

    @GetMapping("/reactivate/{productID}")
    public String reactiveProduct(@PathVariable(value = "productID") Long productID, Principal principal) {
        return productStatusService.updateProductStatus(productID, principal, ProductStatus.ACTIVE);
    }
}
