package com.bulkpurchase.web.controller.admin;

import com.bulkpurchase.domain.dto.SalesDataDTO;
import com.bulkpurchase.domain.service.AdminDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminSalesManageController {

    private final AdminDashboardService adminDashboardService;

    @GetMapping("/statistics")
    public String statisticsPage(Model model) {

        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(30);

        // 최근 30일 판매 데이터
        List<SalesDataDTO> last30DaysSales = adminDashboardService.calculateSalesLast30Days(startDate, endDate);
        // 지난 12개월 판매 데이터
        List<SalesDataDTO> last12MonthsSales = adminDashboardService.calculateSalesLast12Months();
        // 최근 3년 판매 데이터
        List<SalesDataDTO> last3YearsSales = adminDashboardService.calculateSalesLast3Years();

        // 모델에 데이터 추가
        model.addAttribute("last30DaysSales", last30DaysSales);
        model.addAttribute("last12MonthsSales", last12MonthsSales);
        model.addAttribute("last3YearsSales", last3YearsSales);
        return "/admin/statistics";
    }
}
