package com.bulkpurchase.web.controller.product;

import com.bulkpurchase.domain.entity.UserEntity;
import com.bulkpurchase.domain.enums.SalesRegion;
import com.bulkpurchase.domain.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import com.bulkpurchase.domain.entity.ProductEntity;
import com.bulkpurchase.domain.service.ProductService;

import java.security.Principal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
public class ProductController {

    private final ProductService productService;
    private final UserService userService;

    @Autowired
    public ProductController(ProductService productService, UserService userService) {
        this.productService = productService;
        this.userService = userService;
    }

    @GetMapping("/productAdd")
    public String showRegistrationForm(Model model) {
        model.addAttribute("product", new ProductEntity());
        List<SalesRegion> list = Arrays.asList(SalesRegion.values());
        model.addAttribute("allSalesRegions", list);
        return "product/productAdd";
    }

    @PostMapping("/productAdd")
    public String registerProduct(@ModelAttribute ProductEntity product, Principal principal, Model model) {
        if (principal != null) {
            UserEntity currentUser = userService.findByUsername(principal.getName());
            product.setUserEntity(currentUser);
        }

        //검증 오류 결과를 보관
        Map<String, String> errors = new HashMap<>();
        //검증 로직
        if (!StringUtils.hasText(product.getProductName())) {
            errors.put("productName", "상품 이름은 필수입니다.");
        }
        if (!StringUtils.hasText(product.getDescription())) {
            errors.put("description", "상품 설명은 필수입니다.");
        }
        if (product.getSalesRegions().isEmpty()) {
            errors.put("salesRegions", "판매 지역은 필수입니다.");
        }
        if (product.getPrice() == null || product.getPrice() < 1000 || product.getPrice() >
                1000000) {
            errors.put("price", "가격은 1,000 ~ 1,000,000 까지 허용합니다.");
        }
        if (product.getStock() == null || product.getStock() > 9999) {
            errors.put("stock", "수량은 최대 9,999 까지 허용합니다.");
        }
        //특정 필드가 아닌 복합 룰 검증
        if (product.getPrice() != null && product.getStock() != null) {
            long resultPrice = product.getPrice() * product.getStock();
            if (resultPrice < 10000) {
                errors.put("globalError", "가격 * 수량의 합은 10,000원 이상이어야 합니다. 현재값 = " + resultPrice);
            }
        }
        //검증에 실패하면 다시 입력 폼으로
        if (!errors.isEmpty()) {
            model.addAttribute("errors", errors);
            model.addAttribute("product", product);
            List<SalesRegion> list = Arrays.asList(SalesRegion.values());
            model.addAttribute("allSalesRegions", list);
            return "product/productAdd";
        } else {
            productService.saveProduct(product);
            return "redirect:/products";
        }
    }

    @GetMapping("/products")
    public String showProductList(Model model) {
        List<ProductEntity> products = productService.findAllProducts();
        model.addAttribute("products", products);
        return "product/products";
    }
}
