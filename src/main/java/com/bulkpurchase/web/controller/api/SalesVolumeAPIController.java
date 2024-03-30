package com.bulkpurchase.web.controller.api;

import com.bulkpurchase.domain.dto.seller.SalesDataDTO;
import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.enums.UserRole;
import com.bulkpurchase.domain.service.order.OrderService;
import com.bulkpurchase.domain.validator.user.UserAuthValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sales-volume")
public class SalesVolumeAPIController {

    private final UserAuthValidator userAuthValidator;

    private final OrderService orderService;
    private final LocalDate endDate = LocalDate.now().plusDays(1);
    private final LocalDate startDate = endDate.minusDays(30);


    // 최근 30일 판매 데이터
    public ResponseEntity<?> findSalesDataForLast30Days(Principal principal) {
        Long userID = validateSellerRoleAndGetUserId(principal);
        List<SalesDataDTO> last30DaysSales = orderService.calculateSalesLast30DaysBySeller(userID, startDate, endDate);
        last30DaysSales.sort(Comparator.comparing(SalesDataDTO::getPeriod));
        return ResponseEntity.ok(last30DaysSales);
    }


    // 지난 12개월 판매 데이터
    public ResponseEntity<?> findSalesDataOverLastYear(Principal principal) {
        Long userID = validateSellerRoleAndGetUserId(principal);

        List<SalesDataDTO> last12MonthsSales = orderService.calculateSalesLast12MonthsBySeller(userID);
        last12MonthsSales.sort(Comparator.comparing(SalesDataDTO::getPeriod));
        return ResponseEntity.ok(last12MonthsSales);
    }

    // 최근 3년 판매 데이터
    public ResponseEntity<?> findSalesDataOverLastThreeYears(Principal principal) {
        Long userID = validateSellerRoleAndGetUserId(principal);
        return ResponseEntity.ok(orderService.calculateSalesLast3YearsBySeller(userID));
    }

    private Long validateSellerRoleAndGetUserId(Principal principal) {
        User currentUser = userAuthValidator.getCurrentUser(principal);
        if (currentUser.getRole() != UserRole.ROLE_판매자) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "판매자만 이용 가능한 서비스입니다.");
        }
        return currentUser.getUserID();
    }
}
