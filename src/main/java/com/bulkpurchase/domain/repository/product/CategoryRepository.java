package com.bulkpurchase.domain.repository.product;

import com.bulkpurchase.domain.entity.product.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Set<Category> findByParentCategoryID(Long parentCategoryID);

    Set<Category> findByParentIsNull();

    @Query("SELECT c FROM Category c LEFT JOIN FETCH c.children WHERE c.parent IS NULL ORDER BY c.name ASC")
    List<Category> findAllWithChildren();
}
