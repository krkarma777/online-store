package com.bulkpurchase.web.controller.seller;

import com.bulkpurchase.domain.entity.product.Product;
import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.service.product.ProductService;
import com.bulkpurchase.domain.validator.user.UserAuthValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class SellerProductManageController {

    private final UserAuthValidator userAuthValidator;
    private final ProductService productService;

    @GetMapping("/seller/products")
    public String manageProducts(Principal principal, Model model) {

        User user = userAuthValidator.getCurrentUser(principal);
        List<Product> products = productService.findByUserOrderByProductIDDesc(user);
        model.addAttribute("products", products);

        return "/seller/productManage/products";
    }
}
