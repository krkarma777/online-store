package com.bulkpurchase.web.controller.seller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/seller/orders")
@RequiredArgsConstructor
public class SellerOrderManageController {

    @GetMapping
    public String manageOrders() {
        return "seller/orderManage/orders";
    }

    @GetMapping("/detail/{orderDetailID}")
    public String orderDetailInfoForSeller(@PathVariable("orderDetailID") Long orderDetailID, Model model) {
        model.addAttribute("orderDetailID", orderDetailID);
        return "seller/orderManage/orderDetailForSeller";
    }
}
