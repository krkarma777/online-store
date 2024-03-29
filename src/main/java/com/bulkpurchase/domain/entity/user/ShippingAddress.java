package com.bulkpurchase.domain.entity.user;

import com.bulkpurchase.domain.dto.user.ShippingAddressDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "shipping_addresses")
@Getter
@Setter
public class ShippingAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(length = 50, nullable = false)
    private String recipientName;

    @Column(length = 255, nullable = false)
    private String address;

    @Column(length = 255, nullable = false)
    private String detailAddress;

    @Column(length = 20, nullable = false)
    private String zipCode;

    @Column(nullable = false)
    private String phoneNumber;

    public ShippingAddress() {
    }

    public ShippingAddress(ShippingAddressDTO shippingAddressDTO, User user) {
        this.id = shippingAddressDTO.getId();
        this.user = user;
        this.recipientName = shippingAddressDTO.getRecipientName();
        this.address = shippingAddressDTO.getAddress();
        this.detailAddress = shippingAddressDTO.getDetailAddress();
        this.zipCode = shippingAddressDTO.getZipCode();
        this.phoneNumber = shippingAddressDTO.getPhoneNumber();
    }
}
