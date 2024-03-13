package com.bulkpurchase.domain.entity.review;

import com.bulkpurchase.domain.entity.order.OrderDetail;
import com.bulkpurchase.domain.entity.product.Product;
import com.bulkpurchase.domain.entity.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Reviews")
@Getter
@Setter
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ProductID", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UserID", nullable = false)
    private User user;

    @NotBlank
    @Column(nullable = false, length = 500)
    private String content;

    @NotNull
    @Column(nullable = false)
    private Integer rating;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private LocalDateTime creationDate = LocalDateTime.now();

    @ElementCollection
    @CollectionTable(name = "review_image_urls", joinColumns = @JoinColumn(name = "reviewID"))
    @Column(name = "imageUrl")
    private List<String> imageUrls;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "OrderDetailID", nullable = false, unique = true)
    private OrderDetail orderDetail;

    public Review(Product product, User user, String content, Integer rating, OrderDetail orderDetail) {
        this.product = product;
        this.user = user;
        this.content = content;
        this.rating = rating;
        this.orderDetail = orderDetail;
    }

    public Review() {
    }

    @Override
    public String toString() {
        return "Review{" +
                "reviewID=" + reviewID +
                ", product=" + product +
                ", user=" + user +
                ", content='" + content + '\'' +
                ", rating=" + rating +
                ", creationDate=" + creationDate +
                ", imageUrls=" + imageUrls +
                ", orderDetail=" + orderDetail +
                '}';
    }
}
