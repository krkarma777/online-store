package com.bulkpurchase.domain.entity.order;

import com.bulkpurchase.domain.entity.product.Product;
import com.bulkpurchase.domain.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Order_Details")
@Getter
@Setter
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderDetailID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "OrderID", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "ProductID", nullable = false)
    private Product product;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = true)
    private String shippingCompany;

    @Column(nullable = true)
    private String shippingNumber;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private OrderStatus status = OrderStatus.PENDING;

    public OrderDetail() {
    }

    public OrderDetail(Long orderDetailID, Order order, Product product, Integer quantity, Double price, String shippingCompany, String shippingNumber, OrderStatus status) {
        this.orderDetailID = orderDetailID;
        this.order = order;
        this.product = product;
        this.quantity = quantity;
        this.price = price;
        this.shippingCompany = shippingCompany;
        this.shippingNumber = shippingNumber;
        this.status = status;
    }
}
