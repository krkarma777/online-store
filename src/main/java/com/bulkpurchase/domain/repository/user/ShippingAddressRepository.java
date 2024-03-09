package com.bulkpurchase.domain.repository.user;

import com.bulkpurchase.domain.entity.user.ShippingAddress;
import com.bulkpurchase.domain.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShippingAddressRepository extends JpaRepository<ShippingAddress, Long> {
    List<ShippingAddress> findByUser(User user);

    Optional<ShippingAddress> findByUserAndId(User user, Long id);
}
