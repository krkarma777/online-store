package com.bulkpurchase.web.service.product;

import com.bulkpurchase.domain.entity.product.Product;
import com.bulkpurchase.domain.service.product.ProductInquiryService;
import com.bulkpurchase.domain.service.review.ReviewService;
import com.bulkpurchase.domain.service.user.FavoriteProductService;
import com.bulkpurchase.domain.service.user.UserService;
import com.bulkpurchase.web.service.CategoryConstructService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.security.Principal;

@Service
@RequiredArgsConstructor
public class ProductDetailService {

    private final UserService userService;
    private final FavoriteProductService favoriteProductService;
    private final CategoryConstructService categoryConstructService;
    private final ReviewService reviewService;
    private final ProductInquiryService productInquiryService;

    public void populateProductDetails(Model model, Principal principal, Product product) {
        model.addAttribute("product", product);
        model.addAttribute("parentCategories", categoryConstructService.getParentCategories(product.getCategory()));
        model.addAttribute("reviews", reviewService.reviewDetailsByProduct(product));
        model.addAttribute("reviewCount", reviewService.countByProductID(product.getProductID()));
        model.addAttribute("averageRating", reviewService.findAverageRatingByProductID(product.getProductID()));
        model.addAttribute("inquiries", productInquiryService.findByProductProductID(product.getProductID()));
        if (principal != null) {
            userService.findByUsername(principal.getName()).ifPresent(user -> {
                model.addAttribute("favoriteProduct", favoriteProductService.findByUserAndProduct(user, product));
                model.addAttribute("isSeller", product.getUser().equals(user));
            });
        }
    }
}
