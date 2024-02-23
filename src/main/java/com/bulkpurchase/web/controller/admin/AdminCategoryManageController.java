package com.bulkpurchase.web.controller.admin;

import com.bulkpurchase.domain.entity.product.Category;
import com.bulkpurchase.domain.service.product.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminCategoryManageController {

    private final CategoryService categoryService;

    // 카테고리 관리 페이지를 조회하는 메서드
    @GetMapping("/category")
    public String categoryManagementPage(Model model) {
        // 모든 카테고리를 계층 구조로 가져오기
        List<Category> categories = categoryService.findAllWithChildren();
        model.addAttribute("categories", categories);
        model.addAttribute("currentCategory", new Category()); // 새 카테고리 추가를 위한 빈 객체
        return "admin/categoryManagement"; // 카테고리 관리 페이지 뷰 이름
    }

    // 카테고리를 저장(추가 또는 수정)하는 메서드
    @PostMapping("/category/save")
    public String saveCategory(@ModelAttribute("currentCategory") Category category,
                               @RequestParam(value = "parent", required = false) Long parentId) {
        if (parentId != null) {
            categoryService.findById(parentId).ifPresent(category::setParent);
        } else {
            category.setParent(null); // 최상위 카테고리로 설정
        }

        categoryService.save(category); // 카테고리 저장
        return "redirect:/admin/category"; // 카테고리 관리 페이지로 리다이렉트
    }
}
