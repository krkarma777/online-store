package com.bulkpurchase.domain.dto;

import lombok.Data;

import java.util.Date;

@Data
public class SubscriptionDTO {
    private Long subscriptionId;
    private Long userId;
    private Date startDate;
    private Date endDate;
    private String status;

}
