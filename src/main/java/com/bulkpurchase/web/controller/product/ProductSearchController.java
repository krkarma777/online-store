package com.bulkpurchase.web.controller.product;

import com.bulkpurchase.domain.entity.product.Product;
import com.bulkpurchase.domain.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class ProductSearchController {

    private final ProductService productService;

    @GetMapping("/product/search")
    public String productSearchView(@RequestParam(value = "q", required = false) String productName, @RequestParam(value = "p", required = false) Integer page, Model model) {
        int size = 12;
        if (page==null) {
            page = 1;
        }
        if (productName == null) {
            return "redirect:/";
        }
        Pageable pageable = PageRequest.of(page-1, size);
        Page<Product> productPage = productService.findPageByProductNameContaining(pageable, productName);
        model.addAttribute("productPage", productPage);
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("page", page);
        model.addAttribute("q", productName);
        return "product/productSearchView";
    }
}
