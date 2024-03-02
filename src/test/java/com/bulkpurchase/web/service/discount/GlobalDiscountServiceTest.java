package com.bulkpurchase.web.service.discount;

import com.bulkpurchase.domain.dto.discount.GlobalDiscountModel;
import com.bulkpurchase.domain.enums.DiscountType;
import com.bulkpurchase.web.policy.discount.GlobalAmountDiscountPolicy;
import com.bulkpurchase.web.policy.discount.GlobalPercentageDiscountPolicy;
import com.bulkpurchase.web.validator.discount.GlobalDiscountValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class GlobalDiscountServiceTest {

    @Mock
    private GlobalAmountDiscountPolicy globalAmountDiscountPolicy;

    @Mock
    private GlobalPercentageDiscountPolicy globalPercentageDiscountPolicy;

    @Mock
    private GlobalDiscountValidator globalDiscountValidator;

    @InjectMocks
    private GlobalDiscountServiceImpl globalDiscountService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testPercentageDiscount() {
        GlobalDiscountModel discountModel = new GlobalDiscountModel();
        discountModel.setDiscountType(DiscountType.PERCENTAGE_DISCOUNT);
        discountModel.setDiscount(10.0); // 10% discount

        when(globalPercentageDiscountPolicy.discount(discountModel, 100.0)).thenReturn(90.0);

        Double finalPrice = globalDiscountService.globalDiscount(discountModel, 100.0);

        assertEquals(90.0, finalPrice);
    }

    @Test
    public void testAmountDiscount() {
        GlobalDiscountModel discountModel = new GlobalDiscountModel();
        discountModel.setDiscountType(DiscountType.AMOUNT_DISCOUNT);
        discountModel.setDiscount(20.0); // $20 discount

        when(globalAmountDiscountPolicy.discount(discountModel, 100.0)).thenReturn(80.0);

        Double finalPrice = globalDiscountService.globalDiscount(discountModel, 100.0);

        assertEquals(80.0, finalPrice);
    }

    @Test
    public void testInvalidDiscount() {
        GlobalDiscountModel discountModel = new GlobalDiscountModel();
        discountModel.setValidFrom(LocalDateTime.now().minusDays(2));
        discountModel.setValidUntil(LocalDateTime.now().minusDays(1));

        when(globalDiscountValidator.isGlobalDiscountValid(discountModel)).thenReturn(false);

        Double finalPrice = globalDiscountService.globalDiscount(discountModel, 100.0);

        assertEquals(100.0, finalPrice);
    }
}
