package com.bulkpurchase.web.controller.product;

import com.bulkpurchase.domain.entity.product.Product;
import com.bulkpurchase.domain.entity.product.ProductInquiry;
import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.service.product.ProductInquiryService;
import com.bulkpurchase.domain.service.product.ProductService;
import com.bulkpurchase.domain.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class ProductInquiryController {

    private final ProductInquiryService productInquiryService;
    private final UserService userService;
    private final ProductService productService;



    @PostMapping("/inquiry/add")
    public String productInquiryAdd(@RequestParam("productID") Long productID, Principal principal, @ModelAttribute ProductInquiry productInquiry) {
        if (principal == null) {
            return "redirect:/login";
        }
        User user = userService.findByUsername(principal.getName());
        Product product = productService.findById(productID).orElse(null);


        if (product == null) {
            return "error/400";
        }

        productInquiry.setUser(user);
        productInquiry.setProduct(product);
        productInquiryService.save(productInquiry);

        return "redirect:/product/" + productID + "#inquiries";
    }
}
