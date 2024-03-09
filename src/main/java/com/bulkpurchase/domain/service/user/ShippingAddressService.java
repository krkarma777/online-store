package com.bulkpurchase.domain.service.user;

import com.bulkpurchase.domain.entity.user.ShippingAddress;
import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.repository.user.ShippingAddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ShippingAddressService {

    private final ShippingAddressRepository shippingAddressRepository;

    public void save(ShippingAddress shippingAddress) {
        shippingAddressRepository.save(shippingAddress);
    }

    public List<ShippingAddress> findByUser(User user) {
        return shippingAddressRepository.findByUser(user);
    }

    public void delete(Long id) {
        shippingAddressRepository.deleteById(id);
    }

    public Optional<ShippingAddress> findByUserAndId(User user, Long id) {
        return shippingAddressRepository.findByUserAndId(user, id);
    }
}
