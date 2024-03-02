package com.bulkpurchase.web.service.discount;

import com.bulkpurchase.domain.entity.discount.GlobalDiscount;
import com.bulkpurchase.domain.enums.DiscountType;
import com.bulkpurchase.web.policy.discount.GlobalAmountDiscountPolicy;
import com.bulkpurchase.web.policy.discount.GlobalPercentageDiscountPolicy;
import com.bulkpurchase.web.validator.discount.GlobalDiscountValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GlobalDiscountWebServiceImpl implements GlobalDiscountWebService {

    private final GlobalAmountDiscountPolicy globalAmountDiscountPolicy;
    private final GlobalPercentageDiscountPolicy globalPercentageDiscountPolicy;
    private final GlobalDiscountValidator globalDiscountValidator;

    @Override
    public Double globalDiscount(GlobalDiscount globalDiscount, Double totalPrice) {
        if (!globalDiscountValidator.isGlobalDiscountValid(globalDiscount, totalPrice)) {
            return totalPrice; // 유효기간 만료
        }
        DiscountType discountType = globalDiscount.getDiscountType();
        if (discountType.equals(DiscountType.PERCENTAGE_DISCOUNT)) {
            return globalPercentageDiscountPolicy.discount(globalDiscount, totalPrice);
        }
        if (discountType.equals(DiscountType.AMOUNT_DISCOUNT)) {
            return globalAmountDiscountPolicy.discount(globalDiscount, totalPrice);
        }

        return totalPrice;
    }
}
