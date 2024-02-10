package com.bulkpurchase.domain.service.order;

import com.bulkpurchase.domain.dto.OrderDetailViewModel;
import com.bulkpurchase.domain.dto.OrderViewModel;
import com.bulkpurchase.domain.dto.ProductViewModel;
import com.bulkpurchase.domain.dto.SalesDataDTO;
import com.bulkpurchase.domain.entity.Order;
import com.bulkpurchase.domain.entity.OrderDetail;
import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.entity.product.Product;
import com.bulkpurchase.domain.repository.order.OrderDetailRepository;
import com.bulkpurchase.domain.repository.order.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
        order.setOrderDate(new Date());
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

    public List<OrderViewModel> getOrderViewModelsByUser(User user) {
        List<OrderViewModel> orderViewModels = new ArrayList<>();

        List<Order> orders = orderRepository.findByUserOrderByOrderIDDesc(user);
        for (Order order : orders) {
            List<OrderDetail> orderDetails = orderDetailRepository.findByOrder(order);

            List<OrderDetailViewModel> orderDetailViewModels = new ArrayList<>();
            for (OrderDetail orderDetail : orderDetails) {
                Product product = orderDetail.getProduct();
                List<String> imageUrls = product.getImageUrls();
                String imageUrl = null;
                if (!imageUrls.isEmpty()) {
                    imageUrl = imageUrls.get(0);
                }
                ProductViewModel productViewModel = new ProductViewModel(
                        product.getProductID(),
                        product.getProductName(),
                        product.getPrice(),
                        product.getDescription(),
                        imageUrl
                );

                OrderDetailViewModel orderDetailViewModel = new OrderDetailViewModel(
                        orderDetail.getOrderDetailID(),
                        productViewModel,
                        orderDetail.getQuantity(),
                        orderDetail.getPrice()
                );

                orderDetailViewModels.add(orderDetailViewModel);
            }

            OrderViewModel orderViewModel = new OrderViewModel(
                    order.getOrderID(),
                    order.getOrderDate(),
                    order.getTotalPrice(),
                    order.getStatus(),
                    orderDetailViewModels
            );

            orderViewModels.add(orderViewModel);
        }

        return orderViewModels;
    }

    public List<OrderViewModel> getOrderViewModels() {
        List<OrderViewModel> orderViewModels = new ArrayList<>();

        List<Order> orders = orderRepository.findAll();
        for (Order order : orders) {
            List<OrderDetail> orderDetails = orderDetailRepository.findByOrder(order);

            List<OrderDetailViewModel> orderDetailViewModels = new ArrayList<>();
            for (OrderDetail orderDetail : orderDetails) {
                Product product = orderDetail.getProduct();
                List<String> imageUrls = product.getImageUrls();
                String imageUrl = null;
                if (!imageUrls.isEmpty()) {
                    imageUrl = imageUrls.get(0);
                }
                ProductViewModel productViewModel = new ProductViewModel(
                        product.getProductID(),
                        product.getProductName(),
                        product.getPrice(),
                        product.getDescription(),
                        imageUrl
                );

                OrderDetailViewModel orderDetailViewModel = new OrderDetailViewModel(
                        orderDetail.getOrderDetailID(),
                        productViewModel,
                        orderDetail.getQuantity(),
                        orderDetail.getPrice()
                );

                orderDetailViewModels.add(orderDetailViewModel);
            }

            OrderViewModel orderViewModel = new OrderViewModel(
                    order.getOrderID(),
                    order.getOrderDate(),
                    order.getTotalPrice(),
                    order.getStatus(),
                    orderDetailViewModels
            );

            orderViewModels.add(orderViewModel);
        }

        return orderViewModels;
    }

}