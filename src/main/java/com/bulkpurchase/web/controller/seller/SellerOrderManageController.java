package com.bulkpurchase.web.controller.seller;

import com.bulkpurchase.domain.entity.order.OrderDetail;
import com.bulkpurchase.domain.service.order.OrderDetailService;
import com.bulkpurchase.domain.service.order.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping("/seller/orders")
@RequiredArgsConstructor
public class SellerOrderManageController {

    private final OrderDetailService orderDetailService;
    private final PaymentService paymentService;

    @GetMapping
    public String manageOrders() {
        return "seller/orderManage/orders";
    }

    @GetMapping("/detail/{orderDetailID}")
    public String orderDetailInfoForSeller(@PathVariable("orderDetailID") Long orderDetailID, Model model) {
        OrderDetail orderDetail = orderDetailService.findByID(orderDetailID)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "OrderDetail not found"));

        model.addAttribute("orderDetail", orderDetail);
        model.addAttribute("order", orderDetail.getOrder());
        model.addAttribute("payment", paymentService.findByOrder(orderDetail.getOrder()));
        return "seller/orderManage/orderDetailForSeller";
    }
}
