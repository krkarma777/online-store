package com.bulkpurchase.domain.repository.order;

import com.bulkpurchase.domain.entity.Order;
import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);

    List<Order> findByUserOrderByOrderIDDesc(User user);

    /* 전체 판매액 */

    // 총 판매액
    @Query("SELECT SUM(od.price * od.quantity) FROM OrderDetail od")
    BigDecimal calculateTotalSales();

    // 연 판매액
    @Query(value = "SELECT SUM(od.price * od.quantity) FROM order_details od JOIN orders o ON od.orderid = o.orderid WHERE EXTRACT(YEAR FROM o.order_date) = :year", nativeQuery = true)
    BigDecimal calculateYearlySales(@Param("year") int year);


    // 한달 판매액
    @Query(value = "SELECT SUM(od.price * od.quantity) FROM order_details od JOIN orders o ON od.orderid = o.orderid WHERE EXTRACT(YEAR FROM o.order_date) = :year AND EXTRACT(MONTH FROM o.order_date) = :month", nativeQuery = true)
    BigDecimal calculateMonthlySales(@Param("year") int year, @Param("month") int month);

    // 하루 판매액
    @Query(value = "SELECT SUM(od.price * od.quantity) FROM ORDER_DETAILS od JOIN orders o ON od.OrderID = o.OrderID JOIN products p ON od.ProductID = p.ProductID WHERE TRUNC(o.order_date) = TRUNC(:date)", nativeQuery = true)
    BigDecimal calculateDailySales(@Param("date") LocalDate date);

    /* 판매자별 판매액 */

    // 총 판매액
    @Query("SELECT SUM(od.price * od.quantity) FROM OrderDetail od WHERE od.product.user.userID = :userID")
    BigDecimal calculateTotalSalesBySeller(@Param("userID") Long userID);

    // 연 판매액
    @Query(value = "SELECT SUM(od.price * od.quantity) FROM order_details od JOIN orders o ON od.orderid = o.orderid JOIN products p ON od.productid = p.productid WHERE EXTRACT(YEAR FROM o.order_date) = :year AND p.userid = :userID", nativeQuery = true)
    BigDecimal calculateYearlySalesBySeller(@Param("year") int year, @Param("userID") Long userID);

    // 한달 판매액
    @Query(value = "SELECT SUM(od.price * od.quantity) FROM order_details od JOIN orders o ON od.orderid = o.orderid JOIN products p ON od.productid = p.productid WHERE EXTRACT(YEAR FROM o.order_date) = :year AND EXTRACT(MONTH FROM o.order_date) = :month AND p.userid = :userID", nativeQuery = true)
    BigDecimal calculateMonthlySalesBySeller(@Param("year") int year, @Param("month") int month, @Param("userID") Long userID);

    // 하루 판매액 (TRUNC 사용)
    @Query(value = "SELECT SUM(od.price * od.quantity) FROM ORDER_DETAILS od JOIN orders o ON od.OrderID = o.OrderID JOIN products p ON od.ProductID = p.ProductID WHERE TRUNC(o.order_date) = TRUNC(:date) AND p.userID = :userID", nativeQuery = true)
    BigDecimal calculateDailySalesBySeller(@Param("date") LocalDate date, @Param("userID") Long userID);

    // 최근 30일간 판매액
    @Query(value = "SELECT EXTRACT(DAY FROM o.order_date) as day, SUM(od.price * od.quantity) as total_sales " +
            "FROM order_details od " +
            "JOIN orders o ON od.orderid = o.orderid " +
            "JOIN products p ON od.productid = p.productid " +
            "WHERE o.order_date BETWEEN :startDate AND :endDate " +
            "AND p.userid = :userID " +
            "GROUP BY EXTRACT(DAY FROM o.order_date)",
            nativeQuery = true)
    List<Object[]> calculateSalesLast30DaysBySeller(@Param("userID") Long userID, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    // 최근 12개월간 판매액
    @Query(value = "SELECT EXTRACT(MONTH FROM o.order_date) as month, SUM(od.price * od.quantity) as total_sales " +
            "FROM order_details od " +
            "JOIN orders o ON od.orderid = o.orderid " +
            "JOIN products p ON od.productid = p.productid " +
            "WHERE o.order_date >= ADD_MONTHS(CURRENT_DATE, -12) " +
            "AND p.userid = :userID " +
            "GROUP BY EXTRACT(MONTH FROM o.order_date)",
            nativeQuery = true)
    List<Object[]> calculateSalesLast12MonthsBySeller(@Param("userID") Long userID);

    // 최근 3년간 판매액
    @Query(value = "SELECT EXTRACT(YEAR FROM o.order_date) as year, SUM(od.price * od.quantity) as total_sales " +
            "FROM order_details od " +
            "JOIN orders o ON od.orderid = o.orderid " +
            "JOIN products p ON od.productid = p.productid " +
            "WHERE o.order_date >= ADD_MONTHS(CURRENT_DATE, -36) " +
            "AND p.userid = :userID " +
            "GROUP BY EXTRACT(YEAR FROM o.order_date)",
            nativeQuery = true)
    List<Object[]> calculateSalesLast3YearsBySeller(@Param("userID") Long userID);


}
