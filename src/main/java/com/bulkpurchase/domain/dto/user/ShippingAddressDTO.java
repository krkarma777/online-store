package com.bulkpurchase.domain.dto.user;

import com.bulkpurchase.domain.entity.user.ShippingAddress;
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

    public ShippingAddressDTO(ShippingAddress shippingAddress) {
        this.id = shippingAddress.getId();
        this.recipientName = shippingAddress.getRecipientName();
        this.address = shippingAddress.getAddress();
        this.detailAddress = shippingAddress.getDetailAddress();
        this.zipCode = shippingAddress.getZipCode();
        this.phoneNumber = shippingAddress.getPhoneNumber();
    }

    public ShippingAddressDTO() {
    }
}
