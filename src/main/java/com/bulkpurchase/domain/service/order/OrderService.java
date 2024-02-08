package com.bulkpurchase.domain.service.order;

import com.bulkpurchase.domain.dto.OrderDetailViewModel;
import com.bulkpurchase.domain.dto.OrderViewModel;
import com.bulkpurchase.domain.dto.ProductViewModel;
import com.bulkpurchase.domain.entity.Order;
import com.bulkpurchase.domain.entity.OrderDetail;
import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.entity.product.Product;
import com.bulkpurchase.domain.repository.order.OrderDetailRepository;
import com.bulkpurchase.domain.repository.order.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.bulkpurchase.domain.enums.OrderStatus.PENDING;

@Service
@RequiredArgsConstructor
@Transactional
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

}