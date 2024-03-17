package com.bulkpurchase.web.controller.order;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {

    @Value("${paypal.client.id}")
    private String paypalClientId;

    @GetMapping("/{cartID}")
    public String orderFormByCart(@PathVariable("cartID") Long cartID, @RequestParam("itemId") List<Long> itemIDs, Model model) {
        model.addAttribute("cartID", cartID);
        model.addAttribute("itemIDs", itemIDs);
        model.addAttribute("paypalClientId", paypalClientId);
        return "order/orderForm";
    }
}
