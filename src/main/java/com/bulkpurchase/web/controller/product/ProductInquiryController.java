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
    @PostMapping("/inquiry/answer")
    public String productInquiryAnswer(@RequestParam("inquiryID") Long inquiryID,@RequestParam("productID") Long productID,@RequestParam("replyContent") String replyContent,  Principal principal) {
        Product product = productService.findById(productID).orElse(null);

        if (principal == null) {
            return "redirect:/login";
        }

        if (product == null) {
            return "error/403";
        }

        if (!product.getUser().equals(userService.findByUsername(principal.getName()))) {
            return "error/403";
        }

        ProductInquiry inquiry = productInquiryService.findById(inquiryID).orElse(null);
        if (inquiry==null) {
            return "error/400";
        }

        inquiry.setReplyContent(replyContent);
        inquiry.setReplyDate(new Date());
        productInquiryService.save(inquiry);

        return "redirect:/product/" + productID + "#inquiries";
    }
}
