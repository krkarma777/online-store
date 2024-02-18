package com.bulkpurchase.web.controller.product;

import com.bulkpurchase.domain.entity.product.Product;
import com.bulkpurchase.domain.entity.product.ProductInquiry;
import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.service.product.ProductInquiryService;
import com.bulkpurchase.domain.service.product.ProductService;
import com.bulkpurchase.domain.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ProductInquiryController {

    private final ProductInquiryService productInquiryService;
    private final UserService userService;
    private final ProductService productService;

    @GetMapping("/product/{productID}/inquiry")
    public String productInquiry(@PathVariable("productID")@ModelAttribute Long productID, Model model) {
        List<ProductInquiry> inquiries = productInquiryService.findByProductProductID(productID);
        model.addAttribute("inquiries", inquiries);

        ProductInquiry productInquiry = new ProductInquiry();
        model.addAttribute("productInquiry", productInquiry);

        return "product/ProductInquiry";
    }

    @PostMapping("/product/{productID}/inquiry")
    public String productInquiryAdd(@PathVariable("productID") Long productID, Principal principal, ProductInquiry productInquiry) {
        User user = userService.findByUsername(principal.getName());
        Product product = productService.findById(productID).orElse(null);

        if (product==null) {
            return "error/403";
        }

        productInquiry.setUser(user);
        productInquiry.setProduct(product);

        productInquiryService.save(productInquiry);

        return "redirect:/product/{productID}/inquiry";
    }
}
