package com.bulkpurchase.web.service.discount;

import com.bulkpurchase.domain.entity.discount.GlobalDiscount;
import com.bulkpurchase.domain.enums.DiscountType;
import com.bulkpurchase.web.policy.discount.GlobalAmountDiscountPolicy;
import com.bulkpurchase.web.policy.discount.GlobalPercentageDiscountPolicy;
import com.bulkpurchase.web.validator.discount.GlobalDiscountValidator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class GlobalDiscountWebServiceTest {

    @Autowired
    private GlobalAmountDiscountPolicy globalAmountDiscountPolicy;

    @Autowired
    private GlobalPercentageDiscountPolicy globalPercentageDiscountPolicy;

    @Autowired
    private GlobalDiscountValidator globalDiscountValidator;

    @Autowired
    private GlobalDiscountWebServiceImpl globalDiscountService;

    @Test
    public void testPercentageDiscount() {
        GlobalDiscount globalDiscount = new GlobalDiscount();
        globalDiscount.setDiscountType(DiscountType.PERCENTAGE_DISCOUNT);
        globalDiscount.setValidFrom(LocalDateTime.now().minusDays(2));
        globalDiscount.setValidUntil(LocalDateTime.now().plusDays(1));
        globalDiscount.setIsActive(true);
        globalDiscount.setDiscount(10.0); // 10%
        globalDiscount.setMinimumOrderAmount(20.0);
        globalDiscount.setMaxDiscountAmount(20.0);

        Double finalPrice = globalDiscountService.globalDiscount(globalDiscount, 100.0);
        assertThat(finalPrice).isEqualTo(90.0);
    }

    @Test
    public void testAmountDiscount() {
        GlobalDiscount globalDiscount = new GlobalDiscount();
        globalDiscount.setDiscountType(DiscountType.AMOUNT_DISCOUNT);
        globalDiscount.setValidFrom(LocalDateTime.now().minusDays(2));
        globalDiscount.setValidUntil(LocalDateTime.now().plusDays(1));
        globalDiscount.setIsActive(true);
        globalDiscount.setDiscount(20.0);
        globalDiscount.setMinimumOrderAmount(20.0);
        globalDiscount.setMaxDiscountAmount(20.0);

        Double finalPrice = globalDiscountService.globalDiscount(globalDiscount, 100.0);

        assertThat(finalPrice).isEqualTo(80.0);
    }

    @Test
    public void testInvalidDiscount() {
        GlobalDiscount globalDiscount = new GlobalDiscount();
        globalDiscount.setValidFrom(LocalDateTime.now().minusDays(2));
        globalDiscount.setValidUntil(LocalDateTime.now().minusDays(1));

        Double finalPrice = globalDiscountService.globalDiscount(globalDiscount, 100.0);

        assertThat(finalPrice).isEqualTo(100.0);
    }
}
