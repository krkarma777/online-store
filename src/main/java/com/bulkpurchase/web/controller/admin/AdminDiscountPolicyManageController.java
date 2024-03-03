package com.bulkpurchase.web.controller.admin;

import com.bulkpurchase.domain.entity.discount.GlobalDiscount;
import com.bulkpurchase.domain.service.GlobalDiscountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminDiscountPolicyManageController {

    private final GlobalDiscountService globalDiscountService;

    @GetMapping("/admin/event/discount")
    public String discountPolicyManageForm(Model model) {
        List<GlobalDiscount> globalDiscountList = globalDiscountService.findAll();
        model.addAttribute("globalDiscountList", globalDiscountList);
        model.addAttribute("globalDiscount", new GlobalDiscount());
        return "admin/discountPolicy";
    }

    @PostMapping("/admin/event/discount/create")
    public String createDiscountPolicy(GlobalDiscount globalDiscount) {
        globalDiscountService.save(globalDiscount);
        return "redirect:/admin/event/discount";
    }
}
