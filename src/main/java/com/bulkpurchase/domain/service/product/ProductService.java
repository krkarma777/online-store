package com.bulkpurchase.domain.service.product;

import com.bulkpurchase.domain.dto.product.ProductForCouponDTO;
import com.bulkpurchase.domain.dto.product.ProductForSalesVolumeSortDTO;
import com.bulkpurchase.domain.entity.product.Product;
import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.enums.ProductStatus;
import com.bulkpurchase.domain.repository.product.ProductRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final EntityManager entityManager;

    public List<Product> findPopularProductsByCategory(Long categoryID) {
        return productRepository.findPopularProductsByCategory(categoryID);
    }

    public List<Product> findPopularProducts() {
        return productRepository.findPopularProducts();
    }

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    public List<Product> findAllProducts() {
        return productRepository.findAllProducts();
    }

    public List<Product> findActiveProduct(ProductStatus status) {
        return productRepository.findActiveProduct(status);
    }

    public Optional<Product> findById(Long productId) {
        return productRepository.findById(productId);
    }

    public List<Product> findByUserOrderByProductIDDesc(User user) {
        return productRepository.findByUserOrderByProductIDDesc(user);
    }

    public void displayPopularProducts() {
        Pageable topTen = PageRequest.of(0, 10); // 상위 10개의 결과만 조회
        List<Object[]> popularProducts = productRepository.findPopularProducts(topTen);
        for (Object[] record : popularProducts) {
            Product product = (Product) record[0];
            Long orderCount = (Long) record[1];
        }
    }

    public void delete(Long productID) {
        productRepository.deleteById(productID);
    }

    public List<Product> findByCategoryCategoryID(Long categoryID) {
        return productRepository.findByCategoryCategoryID(categoryID);
    }

    public List<ProductForCouponDTO> findByProductNameContaining(String productName, User user) {
        List<Product> productList = productRepository.findByProductNameContainingAndUser(productName, user);
        return productList.stream()
                .map(ProductForCouponDTO::new)
                .collect(Collectors.toList());
    }

    public String findProductNameById(Long productId) {
        return productRepository.findProductNameById(productId);
    }

    public Page<Product> findPageByProductNameContaining(Pageable pageable, String productName) {
        return  productRepository.findByProductNameContaining(pageable, productName);
    }

    public Page<ProductForSalesVolumeSortDTO> findProductsBySalesVolume(String productName, Pageable pageable) {
        String sql = "SELECT p.PRODUCTID as productID, p.PRODUCT_NAME as productName, p.PRICE as price, p.STOCK as stock, u.USERNAME as username, SUM(od.quantity) as totalQuantity " +
                "FROM order_details od " +
                "JOIN products p ON od.PRODUCTID = p.PRODUCTID " +
                "JOIN users u ON p.USERID = u.USERID " +
                "WHERE p.product_name LIKE :productName " +
                "GROUP BY p.PRODUCTID, u.USERNAME, p.STOCK, p.PRICE, p.PRODUCT_NAME " +
                "ORDER BY totalQuantity DESC";

        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("productName", "%" + productName + "%");

        // 페이징 처리
        int totalRows = query.getResultList().size(); // 전체 결과 수
        query.setFirstResult((int) pageable.getOffset()); // 페이징 시작점
        query.setMaxResults(pageable.getPageSize()); // 페이지 크기

        List<Object[]> results = query.getResultList();
        List<ProductForSalesVolumeSortDTO> products = new ArrayList<>();
        for (Object[] result : results) {
            User user = new User();
            user.setUsername((String) result[4]);

            products.add(new ProductForSalesVolumeSortDTO(
                    ((Number) result[0]).longValue(), // productID
                    (String) result[1], // productName
                    ((Number) result[2]).doubleValue(), // price
                    ((Number) result[3]).intValue(), // stock
                    null, // imageUrls 추후 추가
                    ((Number) result[5]).longValue(), // totalQuantity
                    user // User=
            ));
        }

        return new PageImpl<>(products, pageable, totalRows);
    }

    public List<String> findImageUrlsByProductId(Long productID) {
        String sql = "SELECT IMAGE_URL FROM KRKARMA777.PRODUCT_IMAGE_URLS WHERE PRODUCTID = :productID";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("productID", productID);
        return query.getResultList();
    }

    public List<ProductForSalesVolumeSortDTO> completeProductDTOs(List<ProductForSalesVolumeSortDTO> products) {
        for (ProductForSalesVolumeSortDTO product : products) {
            List<String> imageUrls = findImageUrlsByProductId(product.getProductID());
            product.setImageUrls(imageUrls);
        }
        return products;
    }

}
