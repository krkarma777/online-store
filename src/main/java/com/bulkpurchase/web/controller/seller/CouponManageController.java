package com.bulkpurchase.web.controller.seller;

import com.bulkpurchase.domain.dto.ProductIDandNameDTO;
import com.bulkpurchase.domain.entity.coupon.Coupon;
import com.bulkpurchase.domain.entity.coupon.CouponApplicableProduct;
import com.bulkpurchase.domain.entity.product.Product;
import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.enums.CouponType;
import com.bulkpurchase.domain.service.coupon.CouponApplicableProductService;
import com.bulkpurchase.domain.service.coupon.CouponService;
import com.bulkpurchase.domain.service.product.ProductService;
import com.bulkpurchase.domain.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class CouponManageController {

    private final UserService userService;
    private final CouponService couponService;
    private final CouponApplicableProductService couponApplicableProductService;
    private final ProductService productService;

    @GetMapping("/coupon/list")
    public String couponList(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        List<Coupon> couponList = couponService.findByUser(user);
        model.addAttribute("couponList", couponList);

        Coupon coupon = new Coupon();
        model.addAttribute("coupon", coupon);

        CouponApplicableProduct couponApplicableProduct = new CouponApplicableProduct();
        model.addAttribute("couponApplicableProduct", couponApplicableProduct);

        return "/seller/couponManage/couponList";
    }

    @PostMapping("/coupon/generate")
    public String generateCoupon(@RequestParam("validFromDate") String validFromDate,
                                 @RequestParam("validFromTime") String validFromTime,
                                 @RequestParam("validUntilDate") String validUntilDate,
                                 @RequestParam("validUntilTime") String validUntilTime,
                                 Coupon coupon,
                                 Principal principal) {
        LocalDate startDate = LocalDate.parse(validFromDate);
        LocalTime startTime = LocalTime.parse(validFromTime);
        LocalDateTime validFrom = LocalDateTime.of(startDate, startTime);

        LocalDate endDate = LocalDate.parse(validUntilDate);
        LocalTime endTime = LocalTime.parse(validUntilTime);
        LocalDateTime validUntil = LocalDateTime.of(endDate, endTime);

        User user = userService.findByUsername(principal.getName());

        coupon.setValidFrom(validFrom);
        coupon.setValidUntil(validUntil);
        coupon.setCreatedBy(user);
        coupon.setCode(UUID.randomUUID().toString());

        couponService.save(coupon);

        return "redirect:/coupon/list";
    }



    @PostMapping("/coupon/edit")
    public String couponEdit(@RequestParam("validUntilDate") String validUntilDate,
                             @RequestParam("validUntilTime") String validUntilTime,
                             @RequestParam("minimumOrderAmount") Double minimumOrderAmount,
                             @RequestParam("quantity") Integer quantity,
                             @RequestParam("couponID") Long couponID,
                             @RequestParam("name") String name,
                             @RequestParam("description") String description,
                             @RequestParam("maxDiscountAmount") Double maxDiscountAmount) {
        Coupon coupon = couponService.findById(couponID).orElse(null);
        if (coupon == null) {
            return "error/403";
        }

        LocalDate endDate = LocalDate.parse(validUntilDate);
        LocalTime endTime = LocalTime.parse(validUntilTime);
        LocalDateTime validUntil = LocalDateTime.of(endDate, endTime);

        coupon.setValidUntil(validUntil);
        coupon.setMinimumOrderAmount(minimumOrderAmount);
        coupon.setQuantity(quantity);
        coupon.setName(name);
        coupon.setDescription(description);
        coupon.setMaxDiscountAmount(maxDiscountAmount);

        couponService.save(coupon);
        return "redirect:/coupon/list";
    }

    @GetMapping("/coupon/delete/{couponID}")
    public String couponDelete(@PathVariable("couponID") Long couponID, Principal principal) {
        Coupon coupon = couponService.findById(couponID).orElse(null);
        if (coupon == null || principal == null) {
            return "error/403";
        }
        if (!coupon.getCreatedBy().equals(userService.findByUsername(principal.getName()))) {
            return "error/400";
        }
        couponService.delete(coupon);
        return "redirect:/coupon/list";
    }

    @GetMapping("/coupon/select/{couponID}")
    public String selectProductForCouponForm(@PathVariable("couponID") Long couponID, Model model) {
        Coupon coupon = couponService.findById(couponID).orElse(null);
        if (coupon == null) {
            return "error/403";
        }

        List<ProductIDandNameDTO> productDTOs = new ArrayList<>();

        List<CouponApplicableProduct> list = couponApplicableProductService.findByCoupon(coupon);
        for (CouponApplicableProduct couponApplicableProduct : list) {
            Long productId = couponApplicableProduct.getProductId();
            String productName = productService.findProductNameById(productId);
            ProductIDandNameDTO productIDandNameDTO = new ProductIDandNameDTO(productId, productName);
            productDTOs.add(productIDandNameDTO);
        }

        model.addAttribute("productDTOs", productDTOs);
        model.addAttribute("coupon", coupon);

        return "seller/couponManage/couponSelectProduct";
    }
    @PostMapping("/coupon/select")
    public String selectProductForCoupon(@RequestParam("couponID") Long couponID, @RequestParam(value = "productIDs", required = false) List<Long> productIDs) {
        Coupon coupon = couponService.findById(couponID).orElse(null);
        if (coupon == null) {
            return "error/403";
        }

        List<CouponApplicableProduct> couponApplicableProducts = couponApplicableProductService.findByCoupon(coupon);
        for (CouponApplicableProduct couponApplicableProduct : couponApplicableProducts) {
            couponApplicableProductService.delete(couponApplicableProduct);
        }


        for (Long productID : productIDs) {
            CouponApplicableProduct couponApplicableProduct = new CouponApplicableProduct();
            couponApplicableProduct.setCoupon(coupon);
            couponApplicableProduct.setProductId(productID);
            couponApplicableProductService.save(couponApplicableProduct);
        }

        return "redirect:/coupon/list";
    }


}
