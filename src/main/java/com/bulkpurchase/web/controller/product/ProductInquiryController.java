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
import java.util.Date;

@Controller
@RequiredArgsConstructor
public class ProductInquiryController {

    private final ProductInquiryService productInquiryService;
    private final UserService userService;
    private final ProductService productService;

    private static final String REDIRECT_LOGIN = "redirect:/login";
    private static final String ERROR_403 = "error/403";
    private static final String ERROR_400 = "error/400";

    @PostMapping("/inquiry/add")
    public String productInquiryAdd(@RequestParam("productID") Long productID, Principal principal, @ModelAttribute ProductInquiry productInquiry) {
        if (principal == null) return REDIRECT_LOGIN;

        User user = userService.findByUsername(principal.getName()).orElse(null);
        Product product = productService.findById(productID).orElse(null);
        if (product == null) return ERROR_400;

        saveInquiry(productInquiry, user, product);

        return "redirect:/product/" + productID + "#inquiries";
    }

    @PostMapping("/inquiry/answer")
    public String productInquiryAnswer(@RequestParam("inquiryID") Long inquiryID, @RequestParam("productID") Long productID,
                                       @RequestParam("replyContent") String replyContent, Principal principal) {
        if (principal == null) return REDIRECT_LOGIN;

        Product product = productService.findById(productID).orElse(null);
        if (product == null || !isProductOwner(principal.getName(), product)) return ERROR_403;

        return saveInquiryAnswer(inquiryID, replyContent) ? "redirect:/product/" + productID + "#inquiries" : ERROR_400;
    }

    private boolean saveInquiryAnswer(Long inquiryID, String replyContent) {
        return productInquiryService.findById(inquiryID).map(inquiry -> {
            inquiry.setReplyContent(replyContent);
            inquiry.setReplyDate(new Date());
            productInquiryService.save(inquiry);
            return true;
        }).orElse(false);
    }

    private void saveInquiry(ProductInquiry inquiry, User user, Product product) {
        inquiry.setUser(user);
        inquiry.setProduct(product);
        productInquiryService.save(inquiry);
    }

    private boolean isProductOwner(String username, Product product) {
        return userService.findByUsername(username)
                .map(user -> product.getUser().equals(user))
                .orElse(false);
    }
}
