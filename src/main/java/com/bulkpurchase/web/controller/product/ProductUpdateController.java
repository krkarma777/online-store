package com.bulkpurchase.web.controller.product;

import com.bulkpurchase.domain.entity.product.Product;
import com.bulkpurchase.domain.entity.product.UpdateCheck;
import com.bulkpurchase.domain.enums.ErrorPage;
import com.bulkpurchase.domain.enums.SalesRegion;
import com.bulkpurchase.domain.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductUpdateController {

    private final ProductService productService;
    String NOT_FOUND = ErrorPage.NOT_FOUND.getViewName();

    @GetMapping("/update/{productId}")
    public String editForm(@PathVariable Long productId, Model model) {
        Optional<Product> product = productService.findById(productId);
        product.ifPresent(p -> model.addAttribute("product", p));
        return product.isPresent() ? "product/update" : NOT_FOUND;
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
