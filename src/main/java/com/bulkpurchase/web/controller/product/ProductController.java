package com.bulkpurchase.web.controller.product;

import com.bulkpurchase.domain.entity.product.*;
import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.enums.ProductStatus;
import com.bulkpurchase.domain.enums.SalesRegion;
import com.bulkpurchase.domain.service.product.CategoryService;
import com.bulkpurchase.domain.service.product.ProductService;
import com.bulkpurchase.domain.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.*;

@Controller
@Slf4j
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final UserService userService;
    private final CategoryService categoryService;

    @GetMapping("/product/add")
    public String showRegistrationForm(Model model) {
        model.addAttribute("product", new Product());
        List<SalesRegion> list = Arrays.asList(SalesRegion.values());
        model.addAttribute("allSalesRegions", list);
        List<Category> categories = categoryService.findAllWithChildren();
        model.addAttribute("categories", categories);
        return "product/productAdd";
    }

    @PostMapping("/product/add")
    public String addProduct(@Validated(SaveCheck.class) @ModelAttribute Product product, BindingResult bindingResult, Principal principal,
                             RedirectAttributes redirectAttributes, Model model) {
        if (principal != null) {
            User currentUser = userService.findByUsername(principal.getName());
            product.setUser(currentUser);
        }


        List<Category> categories = categoryService.findAllWithChildren();
        model.addAttribute("categories", categories);
        //검증에 실패하면 다시 입력 폼으로
        if (bindingResult.hasErrors()) {
            List<SalesRegion> list = Arrays.asList(SalesRegion.values());
            model.addAttribute("allSalesRegions", list);
            model.addAttribute("product", product);
            return "product/productAdd";
        }

        Product savedProduct = productService.saveProduct(product);
        redirectAttributes.addAttribute("productId", savedProduct.getProductID());
        redirectAttributes.addAttribute("status", true);

        return "redirect:/product/{productId}";

    }

    @GetMapping("/product/{productId}")
    public String productDetail(@PathVariable(value = "productId") Long productId, Model model) {
        Optional<Product> productOpt = productService.findById(productId);
        if (productOpt.isEmpty()) {
            //오류
        } else {
            Product product = productOpt.get();
            model.addAttribute("product", product);

            Category category = product.getCategory();
            List<Category> parentCategories = new ArrayList<>();

            while (category != null) {
                parentCategories.add(category);
                category = category.getParent();
            }

            Collections.reverse(parentCategories);
            model.addAttribute("parentCategories", parentCategories);
        }
        return "product/details";
    }


    @GetMapping("/product/list")
    public String showProductList(Model model) {
        List<Product> products = productService.findAllProducts();
        model.addAttribute("products", products);
        return "product/products";
    }

    @GetMapping("/product/update/{productId}")
    public String editForm(@PathVariable(value = "productId") Long productId, Model model) {
        Optional<Product> product = productService.findById(productId);
        if (product.isEmpty()) {
            //오류
        } else {
            model.addAttribute("product", product.get());
        }
        return "product/update";
    }

    @PostMapping("/product/update/{productId}")
    public String updateSave(@ModelAttribute @Validated(UpdateCheck.class) Product product, BindingResult bindingResult,
                             @PathVariable(value = "productId") Long productId, Model model) {

        if (bindingResult.hasErrors()) {
            List<SalesRegion> list = Arrays.asList(SalesRegion.values());
            model.addAttribute("allSalesRegions", list);
            model.addAttribute("product", product);
            return "product/update";
        }

        Product savedProduct = productService.saveProduct(product);

        return "redirect:/product/" + productId;
    }

    @GetMapping("/products/{userId}")
    public String productsByUser(@PathVariable(value = "userId") Long userId, Model model) {
        Optional<User> byUserid = userService.findByUserid(userId);
        User user = null;
        if (byUserid.isPresent()) {
            user = byUserid.get();
        }
        List<Product> products = productService.findByUserOrderByProductIDDesc(user);
        model.addAttribute("products", products);
        return "product/productsByUser";
    }

    @GetMapping("/product/delete/{productID}")
    public String deleteProduct(@PathVariable(value = "productID") Long productID, Principal principal) {
        Optional<Product> byId = productService.findById(productID);
        User user = userService.findByUsername(principal.getName());
        if (byId.isPresent()) {
            Product product = byId.get();
            if (product.getUser().equals(user)) {
                product.setStatus(ProductStatus.INACTIVE);
                productService.saveProduct(product);
                return "redirect:/seller/products";
            } else {
                return "error/403";
            }
        } else {
            return "error/403";
        }
    }

    @GetMapping("/product/reactivate/{productID}")
    public String reactiveProduct(@PathVariable(value = "productID") Long productID, Principal principal) {
        Optional<Product> byId = productService.findById(productID);
        User user = userService.findByUsername(principal.getName());
        if (byId.isPresent()) {
            Product product = byId.get();
            if (product.getUser().equals(user)) {
                product.setStatus(ProductStatus.ACTIVE);
                productService.saveProduct(product);
                return "redirect:/seller/products";
            } else {
                return "error/403";
            }
        } else {
            return "error/403";
        }
    }
}
