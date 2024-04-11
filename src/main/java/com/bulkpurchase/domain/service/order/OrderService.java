package com.bulkpurchase.domain.service.order;

import com.bulkpurchase.domain.dto.order.OrderViewDTO;
import com.bulkpurchase.domain.dto.orderdetail.OrderDetailViewDTO;
import com.bulkpurchase.domain.dto.product.ProductViewDTO;
import com.bulkpurchase.domain.dto.seller.SalesDataDTO;
import com.bulkpurchase.domain.entity.order.Order;
import com.bulkpurchase.domain.entity.order.OrderDetail;
import com.bulkpurchase.domain.entity.product.Product;
import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.repository.order.OrderDetailRepository;
import com.bulkpurchase.domain.repository.order.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.bulkpurchase.domain.enums.OrderStatus.PENDING;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;

    private final OrderDetailRepository orderDetailRepository;

    public Order save(Order order) {
        return orderRepository.save(order);
    }
    public Order saveOrder(User user, Double totalPrice) {
        Order order = new Order();
        order.setUser(user);
        order.setTotalPrice(totalPrice);
        order.setStatus(PENDING);
        return orderRepository.save(order);
    }

    /* 사이트 전체 판매액 */

    public BigDecimal calculateTotalSales() {
        return orderRepository.calculateTotalSales();
    }
    public BigDecimal calculateYearlySales(int year) {
        return orderRepository.calculateYearlySales(year);
    }
    public BigDecimal calculateMonthlySales(int year, int month) {
        return orderRepository.calculateMonthlySales(year, month);
    }
    public BigDecimal calculateDailySales() {
        LocalDate localDate = LocalDate.now();
        return orderRepository.calculateDailySales(localDate);
    }

    /* 판매자별 판매액 */
    public BigDecimal calculateTotalSalesBySeller(Long userID){
        return orderRepository.calculateTotalSalesBySeller(userID);
    }
    public BigDecimal calculateYearlySalesBySeller(int year, Long userID) {
        return orderRepository.calculateYearlySalesBySeller(year, userID);
    }

    public BigDecimal calculateMonthlySalesBySeller(int year, int month, Long userID) {
        return orderRepository.calculateMonthlySalesBySeller(year, month, userID);
    }

    public List<SalesDataDTO> calculateSalesLast30DaysBySeller(Long userID, LocalDate startDate, LocalDate endDate) {

        List<Object[]> queryResult = orderRepository.calculateSalesLast30DaysBySeller(userID, startDate, endDate);
        List<SalesDataDTO> salesDataDTOList = queryResult.stream()
                .map(result -> new SalesDataDTO(result[0].toString(), (BigDecimal) result[1]))
                .collect(Collectors.toList());

        return salesDataDTOList;
    }

    public List<SalesDataDTO> calculateSalesLast12MonthsBySeller(Long userID) {
        List<Object[]> queryResult = orderRepository.calculateSalesLast12MonthsBySeller(userID);
        List<SalesDataDTO> salesDataDTOList = queryResult.stream()
                .map(result -> new SalesDataDTO(result[0].toString(), (BigDecimal) result[1]))
                .collect(Collectors.toList());

        return salesDataDTOList;
    }

    public List<SalesDataDTO> calculateSalesLast3YearsBySeller(Long userID) {
        List<Object[]> queryResult = orderRepository.calculateSalesLast3YearsBySeller(userID);
        List<SalesDataDTO> salesDataDTOList = queryResult.stream()
                .map(result -> new SalesDataDTO(result[0].toString(), (BigDecimal) result[1]))
                .collect(Collectors.toList());

        return salesDataDTOList;
    }


    public BigDecimal calculateDailySalesBySeller(Long userID) {
        LocalDate localDate = LocalDate.now();
        return orderRepository.calculateDailySalesBySeller(localDate, userID);
    }

    public List<Order> findByUser(User user) {
        return orderRepository.findByUser(user);
    }
    public Optional<Order> findById(Long orderID) {
        return orderRepository.findById(orderID);
    }

    public Map<String, Object> getOrderViewModelsByUser(User user, Pageable pageable) {
        Page<Order> orderPage = orderRepository.findByUserOrderByOrderIDDesc(user, pageable);
        List<OrderViewDTO> orderViewDTOS = orderPage.stream()
                .map(this::convertToOrderViewDTO)
                .toList();
        return Map.of("list", orderViewDTOS, "totalPages", orderPage.getTotalPages());
    }

    public List<OrderViewDTO> getOrderViewModels() {
        return orderRepository.findAll().stream()
                .map(this::convertToOrderViewDTO)
                .collect(Collectors.toList());
    }

    private OrderViewDTO convertToOrderViewDTO(Order order) {
        List<OrderDetailViewDTO> orderDetailViewDTOS = orderDetailRepository.findByOrder(order).stream()
                .map(this::convertToOrderDetailViewDTO)
                .toList();

        return new OrderViewDTO(
                order.getOrderID(),
                order.getOrderDate(),
                order.getTotalPrice(),
                order.getStatus(),
                orderDetailViewDTOS
        );
    }

    private OrderDetailViewDTO convertToOrderDetailViewDTO(OrderDetail orderDetail) {
        Product product = orderDetail.getProduct();
        String imageUrl = product.getImageUrls().stream().findFirst().orElse(null);

        ProductViewDTO productViewDTO = new ProductViewDTO(
                product.getProductID(),
                product.getProductName(),
                product.getPrice(),
                product.getDescription(),
                imageUrl
        );

        return new OrderDetailViewDTO(
                orderDetail.getOrderDetailID(),
                productViewDTO,
                orderDetail.getQuantity(),
                orderDetail.getPrice()
        );
    }

    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    public void delete(Order order) {
        orderRepository.delete(order);
    }

    public List<Order> findByOrderByOrderIDDesc() {
        return orderRepository.findByOrderByOrderIDDesc();
    }
}