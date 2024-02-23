package com.bulkpurchase.web.controller.product;

import com.bulkpurchase.domain.entity.product.Product;
import com.bulkpurchase.domain.entity.product.ProductInquiry;
import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.enums.ErrorPage;
import com.bulkpurchase.domain.service.product.ProductInquiryService;
import com.bulkpurchase.domain.service.product.ProductService;
import com.bulkpurchase.domain.service.user.UserService;
import com.bulkpurchase.web.service.InquirySaveService;
import com.bulkpurchase.web.validator.user.UserAuthValidator;
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

    private final InquirySaveService inquirySaveService;
    private final UserService userService;
    private final ProductService productService;
    private final UserAuthValidator userAuthValidator;

    private static final String REDIRECT_LOGIN = "redirect:/login";
    private final String BAD_REQUEST = ErrorPage.BAD_REQUEST.getViewName();

    @PostMapping("/inquiry/add")
    public String productInquiryAdd(@RequestParam("productID") Long productID, Principal principal, @ModelAttribute ProductInquiry productInquiry) {
        if (principal == null) return REDIRECT_LOGIN;

        User user = userService.findByUsername(principal.getName()).orElse(null);
        Product product = productService.findById(productID).orElse(null);
        if (product == null) return BAD_REQUEST;

        inquirySaveService.saveInquiry(productInquiry, user, product);

        return "redirect:/product/" + productID + "#inquiries";
    }

    @PostMapping("/inquiry/answer")
    public String productInquiryAnswer(@RequestParam("inquiryID") Long inquiryID, @RequestParam("productID") Long productID,
                                       @RequestParam("replyContent") String replyContent, Principal principal) {
        if (principal == null) return REDIRECT_LOGIN;

        Product product = productService.findById(productID).orElse(null);
        if (product == null || !userAuthValidator.isProductOwner(principal, product)) return BAD_REQUEST;

        return inquirySaveService.saveInquiryAnswer(inquiryID, replyContent) ? "redirect:/product/" + productID + "#inquiries" : BAD_REQUEST;
    }
}
