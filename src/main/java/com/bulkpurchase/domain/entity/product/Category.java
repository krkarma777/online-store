package com.bulkpurchase.domain.entity.product;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryID;

    @Column(nullable = false, length = 100)
    private String name;

    // 부모 카테고리와의 관계 설정
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    // 자식 카테고리와의 관계 설정
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("name ASC") // 이름으로 자식 카테고리들을 오름차순 정렬
    private Set<Category> children = new HashSet<>();

    public Category(Long categoryID) {
        this.categoryID = categoryID;
    }

    // 자식 카테고리 추가 메서드
    public void addChildCategory(Category child) {
        children.add(child);
        child.setParent(this);
    }

    // 자식 카테고리 제거 메서드
    public void removeChildCategory(Category child) {
        children.remove(child);
        child.setParent(null);
    }
    public Category() {}

    public Category(String name, Category parent) {
        this.name = name;
        this.parent = parent;
    }

    // 게터와 세터
    public Long getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(Long categoryID) {
        this.categoryID = categoryID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getParent() {
        return parent;
    }

    public void setParent(Category parent) {
        this.parent = parent;
    }

    public Set<Category> getChildren() {
        return children;
    }

    public void setChildren(Set<Category> children) {
        this.children = children;
    }
}
