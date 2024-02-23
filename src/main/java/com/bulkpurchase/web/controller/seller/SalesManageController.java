package com.bulkpurchase.web.controller.seller;

import com.bulkpurchase.domain.dto.SalesDataDTO;
import com.bulkpurchase.domain.service.order.OrderService;
import com.bulkpurchase.domain.service.user.UserService;
import com.bulkpurchase.web.validator.user.UserAuthValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class SalesManageController {

    private final UserAuthValidator userAuthValidator;
    private final OrderService orderService;

    @GetMapping("/seller/sales")
    public String salesView(Model model, Principal principal) {
        Long userID = userAuthValidator.getCurrentUser(principal).getUserID();

        LocalDate endDate = LocalDate.now().plusDays(1);
        LocalDate startDate = endDate.minusDays(30);

        // 최근 30일 판매 데이터
        List<SalesDataDTO> last30DaysSales = orderService.calculateSalesLast30DaysBySeller(userID, startDate, endDate);
        // 날짜순으로 정렬
        last30DaysSales.sort(Comparator.comparing(SalesDataDTO::getPeriod));
        // 지난 12개월 판매 데이터
        List<SalesDataDTO> last12MonthsSales = orderService.calculateSalesLast12MonthsBySeller(userID);
        // 최근 3년 판매 데이터
        List<SalesDataDTO> last3YearsSales = orderService.calculateSalesLast3YearsBySeller(userID);

        // 모델에 데이터 추가
        model.addAttribute("last30DaysSales", last30DaysSales);
        model.addAttribute("last12MonthsSales", last12MonthsSales);
        model.addAttribute("last3YearsSales", last3YearsSales);

        return "/seller/sales";
    }
}
