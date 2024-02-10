package com.bulkpurchase.domain.service.product;

import com.bulkpurchase.domain.entity.product.Category;
import com.bulkpurchase.domain.repository.product.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public void save(Category category) {
        categoryRepository.save(category);
    }

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public Optional<Category> findById(Long parentId) {
        return categoryRepository.findById(parentId);
    }

    public Set<Category> findByParentCategoryID(Long parentId) {
        return categoryRepository.findByParentCategoryID(parentId);
    }

    public Set<Category> findParentCategories() {
        return categoryRepository.findByParentIsNull();
    }

    public List<Category> findAllWithChildren() {
        return categoryRepository.findAllWithChildren();
    }

    public List<Category> findAllSorted() {
        return categoryRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));
    }
}
