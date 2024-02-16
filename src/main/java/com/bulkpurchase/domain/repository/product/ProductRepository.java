package com.bulkpurchase.domain.repository.product;

import com.bulkpurchase.domain.dto.PopularProductDTO;
import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.entity.product.Product;
import com.bulkpurchase.domain.enums.OrderStatus;
import com.bulkpurchase.domain.enums.ProductStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByUserOrderByProductIDDesc(User user);

    @Query("SELECT od.product as product, COUNT(od) as orderCount FROM OrderDetail od GROUP BY od.product ORDER BY orderCount DESC")
    List<Object[]> findPopularProducts(Pageable pageable);

    @Query("select p from Product p WHERE p.status = :status")
    List<Product> findActiveProduct(@Param("status") ProductStatus status);

    List<Product> findByCategoryCategoryID(Long categoryID);

    @Query(value = "SELECT p.* FROM Products p WHERE p.productID IN (" +
            "SELECT od.productID FROM ORDER_DETAILS od GROUP BY od.productID" +
            ")", nativeQuery = true)
    List<Product> findPopularProducts();

    @Query(value = "SELECT p.* FROM Products p WHERE p.productID IN (" +
            "SELECT od.productID FROM ORDER_DETAILS od JOIN Products p2 ON p2.productID = od.productID WHERE p2.categoryID = ?1 " +
            "GROUP BY od.productID" +
            ")", nativeQuery = true)
    List<Product> findPopularProductsByCategory(Long categoryID);

}
