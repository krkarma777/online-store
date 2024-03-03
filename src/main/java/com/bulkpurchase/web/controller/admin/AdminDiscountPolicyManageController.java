package com.bulkpurchase.web.controller.admin;

import com.bulkpurchase.domain.entity.discount.GlobalDiscount;
import com.bulkpurchase.domain.service.GlobalDiscountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

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
    @PostMapping("/admin/event/discount/update")
    public String updateDiscountPolicy(GlobalDiscount globalDiscount) {
        globalDiscountService.save(globalDiscount);
        return "redirect:/admin/event/discount";
    }
    @GetMapping("/discount/{discountId}")
    @ResponseBody
    public GlobalDiscount updateDiscountPolicyForm(@PathVariable("discountId") Long discountId) {
        return globalDiscountService.findById(discountId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "할인 정책을 찾을 수 없습니다."));
    }
}
