package com.bulkpurchase.web.controller.api;

import com.bulkpurchase.domain.dto.user.ShippingAddressDTO;
import com.bulkpurchase.domain.entity.user.ShippingAddress;
import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.service.user.ShippingAddressService;
import com.bulkpurchase.domain.validator.user.UserAuthValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/shipping-address")
public class ShippingAddressController {

    private final ShippingAddressService shippingAddressService;
    private final UserAuthValidator userAuthValidator;

    @GetMapping
    public ResponseEntity<List<ShippingAddressDTO>> shippingAddressListForm(Principal principal) {
        User user = userAuthValidator.getCurrentUser(principal);
        List<ShippingAddress> addressList = shippingAddressService.findByUser(user);
        List<ShippingAddressDTO> shippingAddressDTOS = new ArrayList<>();
        for (ShippingAddress shippingAddress : addressList) {
            ShippingAddressDTO shippingAddressDTO = new ShippingAddressDTO(shippingAddress);
            shippingAddressDTOS.add(shippingAddressDTO);
        }
        return ResponseEntity.status(HttpStatus.OK).body(shippingAddressDTOS);
    }

    @GetMapping("/one")
    public ResponseEntity<?> shippingAddressForUpdate(@RequestParam("addressId") Long addressId, Principal principal) {

        User user = userAuthValidator.getCurrentUser(principal);
        Optional<ShippingAddress> shippingAddressOpt = shippingAddressService.findByUserAndId(user, addressId);
        if (shippingAddressOpt.isPresent()) {
            ShippingAddress shippingAddress = shippingAddressOpt.get();
            ShippingAddressDTO shippingAddressDTO = new ShippingAddressDTO(shippingAddress);
            return ResponseEntity.status(HttpStatus.OK).body(shippingAddressDTO);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "잘못된 요청입니다."));
        }
    }

    @PostMapping
    public ResponseEntity<Map<String,String>> create(@RequestBody ShippingAddressDTO shippingAddressDTO, Principal principal) {
        User user = userAuthValidator.getCurrentUser(principal);
        List<ShippingAddress> addressList = shippingAddressService.findByUser(user);
        if (addressList.size() == 5) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "배송지는 최대 5개까지 등록 가능합니다."));
        }
        shippingAddressService.save(new ShippingAddress(shippingAddressDTO, user));
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "배송지 추가에 성공하셨습니다."));
    }

    @PatchMapping
    public ResponseEntity<Map<String,String>> update(@RequestBody ShippingAddressDTO shippingAddressDTO, Principal principal) {

        User user = userAuthValidator.getCurrentUser(principal);
        Optional<ShippingAddress> shippingAddressOpt = shippingAddressService.findByUserAndId(user, shippingAddressDTO.getId());

        if (shippingAddressOpt.isPresent()) {
            shippingAddressService.save(new ShippingAddress(shippingAddressDTO, user));
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "배송지 수정에 성공하셨습니다."));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "배송지를 삭제할 권한이 없습니다."));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String,String>> delete(@PathVariable("id") Long id , Principal principal) {

        User user = userAuthValidator.getCurrentUser(principal);
        Optional<ShippingAddress> shippingAddressOpt = shippingAddressService.findByUserAndId(user, id);

        if (shippingAddressOpt.isPresent()) {
            shippingAddressService.delete(id);
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("message", "삭제에 성공하셨습니다."));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "배송지를 삭제할 권한이 없습니다."));
        }
    }
}
