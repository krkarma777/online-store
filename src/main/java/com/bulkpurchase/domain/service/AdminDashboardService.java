package com.bulkpurchase.domain.service;

import com.bulkpurchase.domain.dto.seller.SalesDataDTO;
import com.bulkpurchase.domain.enums.OrderStatus;
import com.bulkpurchase.domain.repository.product.ProductRepository;
import com.bulkpurchase.domain.repository.user.UserRepository;
import com.bulkpurchase.domain.repository.order.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminDashboardService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public long getTotalUsers() {
        return userRepository.count();
    }

    public long getTotalProducts() {
        return productRepository.count();
    }

    public long getTotalOrdersExcludingDeliveredAndCancelled() {
        // 제외할 상태 목록 생성
        List<OrderStatus> excludedStatuses = Arrays.asList(OrderStatus.DELIVERED, OrderStatus.CANCELLED);
        // 제외할 상태를 제외한 주문 수 반환
        return orderRepository.countByStatusExcluding(excludedStatuses);
    }
    public List<SalesDataDTO> calculateSalesLast30Days(LocalDate startDate, LocalDate endDate) {

        List<Object[]> queryResult = orderRepository.calculateSalesLast30Days(startDate, endDate);
        List<SalesDataDTO> salesDataDTOList = queryResult.stream()
                .map(result -> new SalesDataDTO(result[0].toString(), (BigDecimal) result[1]))
                .collect(Collectors.toList());

        return salesDataDTOList;
    }

    public List<SalesDataDTO> calculateSalesLast12Months() {
        List<Object[]> queryResult = orderRepository.calculateSalesLast12Months();
        List<SalesDataDTO> salesDataDTOList = queryResult.stream()
                .map(result -> new SalesDataDTO(result[0].toString(), (BigDecimal) result[1]))
                .collect(Collectors.toList());

        return salesDataDTOList;
    }

    public List<SalesDataDTO> calculateSalesLast3Years() {
        List<Object[]> queryResult = orderRepository.calculateSalesLast3Years();
        List<SalesDataDTO> salesDataDTOList = queryResult.stream()
                .map(result -> new SalesDataDTO(result[0].toString(), (BigDecimal) result[1]))
                .collect(Collectors.toList());

        return salesDataDTOList;
    }


}
