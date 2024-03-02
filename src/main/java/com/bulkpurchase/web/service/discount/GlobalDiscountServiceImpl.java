package com.bulkpurchase.web.service.discount;

import com.bulkpurchase.domain.dto.discount.GlobalDiscountModel;
import com.bulkpurchase.domain.enums.DiscountType;
import com.bulkpurchase.web.policy.discount.GlobalAmountDiscountPolicy;
import com.bulkpurchase.web.policy.discount.GlobalPercentageDiscountPolicy;
import com.bulkpurchase.web.validator.discount.GlobalDiscountValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GlobalDiscountServiceImpl implements GlobalDiscountService{

    private final GlobalAmountDiscountPolicy globalAmountDiscountPolicy;
    private final GlobalPercentageDiscountPolicy globalPercentageDiscountPolicy;
    private final GlobalDiscountValidator globalDiscountValidator;

    @Override
    public Double globalDiscount(GlobalDiscountModel globalDiscountModel, Double totalPrice) {

        if (globalDiscountValidator.isGlobalDiscountValid(globalDiscountModel)) {
            return totalPrice; // 유효기간 만료
        }

        DiscountType discountType = globalDiscountModel.getDiscountType();
        if (discountType.equals(DiscountType.PERCENTAGE_DISCOUNT)) {
            return globalPercentageDiscountPolicy.discount(globalDiscountModel, totalPrice);
        }
        if (discountType.equals(DiscountType.AMOUNT_DISCOUNT)) {
            return globalAmountDiscountPolicy.discount(globalDiscountModel, totalPrice);
        }

        return totalPrice;
    }
}
