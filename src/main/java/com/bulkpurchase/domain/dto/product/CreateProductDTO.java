package com.bulkpurchase.domain.dto.product;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import java.util.List;

@Getter
@Setter
public class CreateProductDTO {
    @NotEmpty
    private String productName;

    @NotEmpty
    private String description;

    @NotNull
    @Range(min = 1000, max = 1000000)
    private Double price;

    @NotNull
    private Integer stock;

    private List<String> imageUrls;

    private Long categoryID;
}
