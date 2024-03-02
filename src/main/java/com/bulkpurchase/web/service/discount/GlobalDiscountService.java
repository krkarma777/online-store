package com.bulkpurchase.web.service.discount;

import com.bulkpurchase.domain.dto.discount.GlobalDiscountModel;

import java.time.LocalDateTime;

public interface GlobalDiscountService {

    void globalDiscount(GlobalDiscountModel globalDiscountModel);
}
