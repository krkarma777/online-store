package com.bulkpurchase.web.controller.admin;

import com.bulkpurchase.domain.entity.product.Product;
import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.enums.ProductStatus;
import com.bulkpurchase.domain.enums.UserStatus;
import com.bulkpurchase.domain.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminProductManageController {

    private final ProductService productService;

    @GetMapping("/product")
    public String productManagementPage(Model model) {
        List<Product> products = productService.findAllProducts();
        model.addAttribute("products", products);

        return "/admin/productManagement";
    }

    @PostMapping("/product/status")
    @ResponseBody
    public ResponseEntity<?> changeStatus(@RequestParam("productID") Long productID, @RequestParam("status") ProductStatus status) {
        Product product = productService.findById(productID)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));

        product.setStatus(status);

        productService.saveProduct(product);

        return ResponseEntity.noContent().build();
    }
}
