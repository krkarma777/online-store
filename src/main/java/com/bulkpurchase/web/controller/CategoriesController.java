package com.bulkpurchase.web.controller;


import com.bulkpurchase.domain.service.order.OrderDetailService;
import com.bulkpurchase.domain.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@Controller
@RequiredArgsConstructor
public class CategoriesController {

    private final ProductService productService;
    private final OrderDetailService orderDetailService;

    @GetMapping("/categories/{categoryID}")
    public String categoryView(@PathVariable(value = "categoryID") Long categoryID, Model model) {
        var productList = productService.findByCategoryCategoryID(categoryID);
        model.addAttribute("productList",productList);

        var popularProductList = productService.findPopularProductsByCategory(categoryID);
        model.addAttribute("popularProductList",popularProductList);

        return "category/categoryView";
    }

}
