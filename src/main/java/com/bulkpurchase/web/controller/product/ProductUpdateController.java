package com.bulkpurchase.web.controller.product;

import com.bulkpurchase.domain.entity.product.Product;
import com.bulkpurchase.domain.entity.product.UpdateCheck;
import com.bulkpurchase.domain.enums.SalesRegion;
import com.bulkpurchase.domain.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductUpdateController {

    private final ProductService productService;

    @GetMapping("/update/{productId}")
    public String editForm(@PathVariable Long productId, Model model) {
        Optional<Product> product = productService.findById(productId);
        product.ifPresent(p -> model.addAttribute("product", p));
        return product.isPresent() ? "product/update" : "error/404"; // 오류 처리 페이지로 리다이렉트 또는 다른 방식으로 처리
    }

    @PostMapping("/update")
    public String updateSave(@ModelAttribute @Validated(UpdateCheck.class) Product product, BindingResult bindingResult,
                             Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("allSalesRegions", SalesRegion.values());
            return "product/update";
        }

        Product savedProduct = productService.saveProduct(product);

        return "redirect:/product/" + savedProduct.getProductID(); // 수정된 상품의 ID로 리다이렉트
    }

}
