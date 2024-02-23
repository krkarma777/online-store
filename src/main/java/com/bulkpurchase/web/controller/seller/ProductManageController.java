package com.bulkpurchase.web.controller.seller;

import com.bulkpurchase.domain.entity.product.Product;
import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.enums.ProductStatus;
import com.bulkpurchase.domain.service.product.ProductService;
import com.bulkpurchase.domain.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ProductManageController {

    private final UserService userService;
    private final ProductService productService;

    @GetMapping("/seller/products")
    public String manageProducts(Principal principal, Model model) {

        User user = userService.findByUsername(principal.getName()).orElse(null);
        List<Product> products = productService.findByUserOrderByProductIDDesc(user);
        model.addAttribute("products", products);

//        for (Product product : products) {
//            ProductStatus status = product.getStatus();
//        }
        return "/seller/productManage/products";
    }
}
