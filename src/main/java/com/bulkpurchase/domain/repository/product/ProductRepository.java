package com.bulkpurchase.domain.repository.product;

import com.bulkpurchase.domain.dto.product.ProductForSalesVolumeSortDTO;
import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.entity.product.Product;
import com.bulkpurchase.domain.enums.ProductStatus;
import org.springframework.data.domain.Page;
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

    List<Product> findByProductNameContainingAndUser(String productName,User user);
    
    
    @Query("select p.productName from Product p WHERE p.productID = :productID")
    String findProductNameById(@Param("productID") Long productID);

    Page<Product> findByProductNameContaining(Pageable pageable, String productName);

    @Query("SELECT p FROM Product p ORDER BY p.productID desc ")
    List<Product> findAllProducts();

//    @Query(value = "SELECT new com.bulkpurchase.domain.dto.product.ProductForSalesVolumeSortDTO" +
//            "(p.productID, p.productName, p.price, p.stock, p.user.username, p.imageUrls,SUM(od.quantity) , p.user)" +
//            "FROM OrderDetail od JOIN od.product p " +
//            "WHERE p.productName LIKE %:productName% " +
//            "GROUP BY p.productID, p.productName, p.price, p.stock, p.user.username,  p.imageUrls,od.order, p.user " +
//            "ORDER BY SUM(od.quantity) DESC",
//            countQuery = "SELECT COUNT(DISTINCT p.productID) " +
//                    "FROM OrderDetail od JOIN od.product p " +
//                    "WHERE p.productName LIKE %:productName%",
//            nativeQuery = false)
//    Page<ProductForSalesVolumeSortDTO> findByProductNameContainingAndOrderBySalesVolume(@Param("productName") String productName, Pageable pageable);


    Page<Product> findByUser(User user, Pageable pageable);
}
