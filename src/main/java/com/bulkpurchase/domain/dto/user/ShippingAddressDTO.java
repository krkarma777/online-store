package com.bulkpurchase.domain.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShippingAddressDTO {

    private Long id;

    private String recipientName;

    private String address;

    private String detailAddress;

    private String zipCode;

    private String phoneNumber;
}
