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
    public String editForm(@PathVariable(value = "productId") Long productId, Model model) {
        Optional<Product> product = productService.findById(productId);
        if (product.isEmpty()) {
            //오류
        } else {
            model.addAttribute("product", product.get());
        }
        return "product/update";
    }

    @PostMapping("/update/{productId}")
    public String updateSave(@ModelAttribute @Validated(UpdateCheck.class) Product product, BindingResult bindingResult,
                             @PathVariable(value = "productId") Long productId, Model model) {

        if (bindingResult.hasErrors()) {
            List<SalesRegion> list = Arrays.asList(SalesRegion.values());
            model.addAttribute("allSalesRegions", list);
            model.addAttribute("product", product);
            return "product/update";
        }

        Product savedProduct = productService.saveProduct(product);

        return "redirect:/product/" + productId;
    }

}
