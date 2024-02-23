package com.bulkpurchase.web.service;

import com.bulkpurchase.domain.entity.product.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryConstructService {

    public List<Category> getParentCategories(Category category) {
        List<Category> parentCategories = new ArrayList<>();
        while (category != null) {
            parentCategories.add(0, category);
            category = category.getParent();
        }
        return parentCategories;
    }
}
