package com.bulkpurchase.web.service.discount;

import com.bulkpurchase.domain.dto.discount.GlobalDiscountModel;

public interface GlobalDiscountService {

    Double globalDiscount(GlobalDiscountModel globalDiscountModel, Double totalprice);
}
