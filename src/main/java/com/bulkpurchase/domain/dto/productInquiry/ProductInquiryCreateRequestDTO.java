package com.bulkpurchase.domain.dto.productInquiry;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductInquiryCreateRequestDTO {

    private Long productID;

    @NotEmpty
    private String inquiryContent;
}
