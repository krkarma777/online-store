package com.bulkpurchase.web.controller.payment;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PaymentController {

    @Value("${paypal.client.id}")
    private String paypalClientId;

    @GetMapping("/paypal-test")
    public String paypalTest(Model model) {
        model.addAttribute("paypalClientId", paypalClientId);
        return "order/paymentGateway/paypal-test";
    }
}
