package com.bulkpurchase.domain.service.validator;

import com.bulkpurchase.domain.entity.product.Product;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class ProductValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return Product.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        Product product = (Product) target;

        //검증 로직
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "productName","required");

        if (product.getPrice() == null || product.getPrice() < 1000 || product.getPrice() > 1000000) {
            errors.rejectValue("price","range", new Object[]{1000,100000},null);
        }
        if (!StringUtils.hasText(product.getDescription())) {
            errors.rejectValue("description","required");
        }
        if (product.getSalesRegions().isEmpty()) {
            errors.rejectValue("salesRegions","required");
        }
        if (product.getStock() == null || product.getStock() > 9999) {
            errors.rejectValue("stock","max", new Object[]{99999},null);
        }
        //특정 필드가 아닌 복합 룰 검증
        if (product.getPrice() != null && product.getStock() != null) {
            double resultPrice = product.getPrice() * product.getStock();
            if (resultPrice < 10000) {
                errors.reject("totalPriceMin", new Object[]{10000, resultPrice},null);
            }
        }

    }
}
