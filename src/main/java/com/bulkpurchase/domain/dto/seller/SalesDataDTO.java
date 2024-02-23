package com.bulkpurchase.domain.dto.seller;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class SalesDataDTO {
    private String period; // 날짜, 월 또는 년을 나타낼 문자열
    private BigDecimal totalSales; // 해당 기간의 총 판매액

    public SalesDataDTO(String period, BigDecimal totalSales) {
        this.period = period;
        this.totalSales = totalSales;
    }

    @Override
    public String toString() {
        return "SalesDataDTO{" +
                "period='" + period + '\'' +
                ", totalSales=" + totalSales +
                '}';
    }
}
