package com.bulkpurchase.web.controller.seller;

import com.bulkpurchase.domain.dto.product.ProductIDandNameDTO;
import com.bulkpurchase.domain.entity.coupon.Coupon;
import com.bulkpurchase.domain.entity.coupon.CouponApplicableProduct;
import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.service.coupon.CouponApplicableProductService;
import com.bulkpurchase.domain.service.coupon.CouponService;
import com.bulkpurchase.domain.service.product.ProductService;
import com.bulkpurchase.domain.validator.user.UserAuthValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/seller/coupon")
@RequiredArgsConstructor
public class SellerCouponManageController {

    private final CouponService couponService;
    private final CouponApplicableProductService couponApplicableProductService;
    private final ProductService productService;
    private final UserAuthValidator userAuthValidator;

    @GetMapping("/list")
    public String couponList(Model model, Principal principal) {
        User user = userAuthValidator.getCurrentUser(principal);
        List<Coupon> couponList = couponService.findByUser(user);
        model.addAttribute("couponList", couponList);
        model.addAttribute("coupon", new Coupon());
        return "seller/couponManage/couponList";
    }

    @PostMapping("/generate")
    public String generateCoupon(@RequestParam("validFromDate") String validFromDate,
                                 @RequestParam("validFromTime") String validFromTime,
                                 @RequestParam("validUntilDate") String validUntilDate,
                                 @RequestParam("validUntilTime") String validUntilTime,
                                 Coupon coupon, Principal principal) {
        LocalDateTime validFrom = LocalDateTime.of(LocalDate.parse(validFromDate), LocalTime.parse(validFromTime));
        LocalDateTime validUntil = LocalDateTime.of(LocalDate.parse(validUntilDate), LocalTime.parse(validUntilTime));

        User user = userAuthValidator.getCurrentUser(principal);
        coupon.setValidFrom(validFrom);
        coupon.setValidUntil(validUntil);
        coupon.setCreatedBy(user);
        coupon.setCode(UUID.randomUUID().toString());

        couponService.save(coupon);
        return "redirect:/seller/couponManage/list";
    }

    @PostMapping("/edit")
    public String couponEdit(@RequestParam("couponID") Long couponID,
                             @RequestParam("validUntilDate") String validUntilDate,
                             @RequestParam("validUntilTime") String validUntilTime,
                             @RequestParam("minimumOrderAmount") Double minimumOrderAmount,
                             @RequestParam("quantity") Integer quantity,
                             @RequestParam("name") String name,
                             @RequestParam("description") String description,
                             @RequestParam("maxDiscountAmount") Double maxDiscountAmount) {
        Coupon coupon = couponService.findById(couponID)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Coupon not found"));

        LocalDateTime validUntil = LocalDateTime.of(LocalDate.parse(validUntilDate), LocalTime.parse(validUntilTime));
        coupon.updateDetails(validUntil, minimumOrderAmount, quantity, name, description, maxDiscountAmount);

        couponService.save(coupon);
        return "redirect:/seller/couponManage/list";
    }

    @GetMapping("/delete/{couponID}")
    public String couponDelete(@PathVariable("couponID") Long couponID, Principal principal) {
        Coupon coupon = couponService.findById(couponID)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Coupon not found"));
        User currentUser = userAuthValidator.getCurrentUser(principal);

        if (!coupon.getCreatedBy().equals(currentUser)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not authorized to delete this coupon");
        }

        couponService.delete(coupon);
        return "redirect:/seller/couponManage/list";
    }

    @GetMapping("/select/{couponID}")
    public String selectProductForCouponForm(@PathVariable("couponID") Long couponID, Model model) {
        Coupon coupon = couponService.findById(couponID)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Coupon not found"));

        List<ProductIDandNameDTO> productDTOs = couponApplicableProductService.findByCoupon(coupon).stream()
                .map(cap -> new ProductIDandNameDTO(cap.getProductId(), productService.findProductNameById(cap.getProductId())))
                .collect(Collectors.toList());

        model.addAttribute("productDTOs", productDTOs);
        model.addAttribute("coupon", coupon);

        return "seller/couponManage/couponSelectProduct";
    }

    @PostMapping("/select")
    @Transactional
    public String selectProductForCoupon(@RequestParam("couponID") Long couponID,
                                         @RequestParam(value = "productIDs", required = false) List<Long> productIDs) {
        Coupon coupon = couponService.findById(couponID)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Coupon not found"));

        couponApplicableProductService.deleteByCoupon(coupon);

        if (productIDs != null) {
            productIDs.forEach(productID -> {
                CouponApplicableProduct cap = new CouponApplicableProduct(coupon, productID);
                couponApplicableProductService.save(cap);
            });
        }

        return "redirect:/seller/couponManage/list";
    }
}
