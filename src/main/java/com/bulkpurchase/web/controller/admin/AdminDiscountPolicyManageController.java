package com.bulkpurchase.web.controller.admin;

import com.bulkpurchase.domain.entity.discount.GlobalDiscount;
import com.bulkpurchase.domain.service.GlobalDiscountService;
import com.bulkpurchase.web.service.discount.GlobalDiscountWebService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminDiscountPolicyManageController {

    private final GlobalDiscountService globalDiscountService;

    @GetMapping("/admin/event/discount")
    public String discountPolicyManageForm(Model model) {

        List<GlobalDiscount> globalDiscounts = globalDiscountService.findAll();
        model.addAttribute("globalDiscounts", globalDiscounts);
        return "admin/discountPolicy";
    }
}
