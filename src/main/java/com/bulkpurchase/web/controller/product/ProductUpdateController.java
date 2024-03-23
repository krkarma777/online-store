package com.bulkpurchase.web.controller.product;

import com.bulkpurchase.domain.entity.product.Product;
import com.bulkpurchase.domain.entity.product.UpdateCheck;
import com.bulkpurchase.domain.enums.ErrorPage;
import com.bulkpurchase.domain.enums.SalesRegion;
import com.bulkpurchase.domain.service.product.ProductService;
import com.bulkpurchase.domain.validator.user.UserAuthValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductUpdateController {

    private final ProductService productService;
    private final UserAuthValidator userAuthValidator;

    String NOT_FOUND = ErrorPage.NOT_FOUND.getViewName();
    String UNAUTHORIZED = ErrorPage.UNAUTHORIZED.getViewName();

    @GetMapping("/update/{productID}")
    public String editForm(@PathVariable("productID") Long productID, Model model, Principal principal) {
        Optional<Product> productOpt = productService.findById(productID);

        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            if (userAuthValidator.isProductOwner(principal, product)) {
                model.addAttribute("product", product);
                return "product/update";
            } else {
                return UNAUTHORIZED;
            }
        } else {
            return NOT_FOUND;
        }
    }

    @PostMapping("/update")
    public String updateSave(@ModelAttribute @Validated(UpdateCheck.class) Product product, BindingResult bindingResult,
                             Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("allSalesRegions", SalesRegion.values());
            return "product/update";
        }
        Product savedProduct = productService.save(product);

        return "redirect:/product/" + savedProduct.getProductID(); // 수정된 상품의 ID로 리다이렉트
    }

}
