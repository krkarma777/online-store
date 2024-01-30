package com.bulkpurchase.web.controller.product;

import com.bulkpurchase.domain.entity.product.Product;
import com.bulkpurchase.domain.entity.User;
import com.bulkpurchase.domain.entity.product.SaveCheck;
import com.bulkpurchase.domain.entity.product.UpdateCheck;
import com.bulkpurchase.domain.enums.SalesRegion;
import com.bulkpurchase.domain.service.ImageStorageService;
import com.bulkpurchase.domain.service.ProductService;
import com.bulkpurchase.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
@Slf4j
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final UserService userService;
    private final ImageStorageService imageStorageService;

    @GetMapping("/product/add")
    public String showRegistrationForm(Model model) {
        model.addAttribute("product", new Product());
        List<SalesRegion> list = Arrays.asList(SalesRegion.values());
        model.addAttribute("allSalesRegions", list);
        return "product/productAdd";
    }

    @PostMapping("/product/add")
    public String addProduct(@Validated(SaveCheck.class) @ModelAttribute Product product, BindingResult bindingResult, Principal principal,
                             RedirectAttributes redirectAttributes, Model model, @RequestParam("image") MultipartFile image) {
        if (principal != null) {
            User currentUser = userService.findByUsername(principal.getName());
            product.setUser(currentUser);
        }

        log.info("product = {}", product);

        //검증에 실패하면 다시 입력 폼으로
        if (bindingResult.hasErrors()) {
            log.info("bindingResult = {}", bindingResult);
            List<SalesRegion> list = Arrays.asList(SalesRegion.values());
            model.addAttribute("allSalesRegions", list);
            model.addAttribute("product", product);
            return "product/productAdd";
        }

        log.info("image1={}", image);
        // 이미지 처리
        if (!image.isEmpty()) {
            String imageUrl = imageStorageService.store(image);
            product.setImageURL(imageUrl);
        }

        Product savedProduct = productService.saveProduct(product);
        redirectAttributes.addAttribute("productId", savedProduct.getProductID());
        redirectAttributes.addAttribute("status", true);

        return "redirect:/product/{productId}";

    }

    @GetMapping("/product/{productId}")
    public String productDetail(@PathVariable(value = "productId") Long productId, Model model) {
        Optional<Product> product = productService.findById(productId);
        if (product.isEmpty()) {
            //오류
        } else {
            model.addAttribute("product", product.get());
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
                             @PathVariable(value = "productId") Long productId, Model model,@RequestParam("image") MultipartFile image) {
        log.info("product = {}" , product);
        if (bindingResult.hasErrors()) {
            List<SalesRegion> list = Arrays.asList(SalesRegion.values());
            model.addAttribute("allSalesRegions", list);
            model.addAttribute("product", product);
            return "product/update";
        }

        log.info("image1={}", image);
        // 이미지 처리
        if (!image.isEmpty()) {
            imageStorageService.delete(product.getImageURL());
            String imageUrl = imageStorageService.store(image);
            product.setImageURL(imageUrl);
        }

        Product savedProduct = productService.saveProduct(product);

        return "redirect:/product/" +productId;
    }


}
