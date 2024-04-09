package com.bulkpurchase.domain.service.order;

import com.bulkpurchase.domain.dto.orderdetail.OrderDetailManagementResponseDTO;
import com.bulkpurchase.domain.dto.orderdetail.OrderDetailNameAndIdDTO;
import com.bulkpurchase.domain.entity.order.Order;
import com.bulkpurchase.domain.entity.order.OrderDetail;
import com.bulkpurchase.domain.entity.product.Product;
import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.repository.order.OrderDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderDetailService {

    private final OrderDetailRepository orderDetailRepository;

    public Map<String, Object> findBySeller(User user, Pageable pageable) {
        Page<OrderDetail> orderDetailPage = orderDetailRepository.findByProductUser(user, pageable);
        return Map.of(
                "orderDetails", orderDetailPage.getContent().stream().map(OrderDetailManagementResponseDTO::new).toList(),
                "totalPages", orderDetailPage.getTotalPages());
    }

    public List<OrderDetailNameAndIdDTO> findTop5RecentOrderDetailsByUser(User user) {
        return orderDetailRepository.findOrderIDAndProductNameByUser(user, PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "orderDetailID")));
    }

    public OrderDetail save(Order order, Product product, Integer quantity) {
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrder(order);
        orderDetail.setQuantity(quantity);
        orderDetail.setProduct(product);
        orderDetail.setPrice(product.getPrice() * quantity);
        return orderDetailRepository.save(orderDetail);
    }

    public List<OrderDetail> findByOrder(Order order) {
        return orderDetailRepository.findByOrder(order);
    }

    public List<OrderDetail> findByProductOrderByOrderDetailIDDesc(Product product) {
        return orderDetailRepository.findByProductOrderByOrderDetailIDDesc(product);
    }

    public List<OrderDetail> findAll() {
        return orderDetailRepository.findAll();
    }

    public Optional<OrderDetail> findByID(Long orderDetailID) {
        return orderDetailRepository.findById(orderDetailID);
    }

    public void save(OrderDetail orderDetail) {
        orderDetailRepository.save(orderDetail);
    }
}
