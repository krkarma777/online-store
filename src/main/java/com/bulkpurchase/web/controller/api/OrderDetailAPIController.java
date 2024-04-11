package com.bulkpurchase.web.controller.api;

import com.bulkpurchase.domain.dto.order.OrderDetailResponseDTO;
import com.bulkpurchase.domain.dto.order.OrderResponseDTO;
import com.bulkpurchase.domain.dto.order.PaymentResponseDTO;
import com.bulkpurchase.domain.dto.orderdetail.OrderDetailNameAndIdDTO;
import com.bulkpurchase.domain.dto.orderdetail.OrderDetailStatusUpdateRequestDTO;
import com.bulkpurchase.domain.entity.order.OrderDetail;
import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.service.order.OrderDetailService;
import com.bulkpurchase.domain.service.order.PaymentService;
import com.bulkpurchase.domain.validator.user.UserAuthValidator;
import com.bulkpurchase.web.service.order.OrderStatusUpdateService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/order-detail")
public class OrderDetailAPIController {

    private final OrderDetailService orderDetailService;
    private final OrderStatusUpdateService orderStatusUpdateService;
    private final PaymentService paymentService;
    private final UserAuthValidator userAuthValidator;

    @GetMapping("/seller")
    public ResponseEntity<?> manageForm(Principal principal, @RequestParam(value = "page", defaultValue = "1") Integer page) {
        page = (page <= 1) ? 1 : page;

        User user = userAuthValidator.getCurrentUser(principal);

        Sort sort = Sort.by(Sort.Direction.DESC, "orderDetailID");

        PageRequest pageRequest = PageRequest.of(page - 1, 10, sort);

        // 조회된 데이터와 함께 총 페이지 수를 맵으로 구성하여 반환합니다.
        // 'orderDetails' 키에는 OrderDetail 객체들을 OrderDetailManagementResponseDTO 객체로 변환한 리스트를 할당합니다.
        // 'totalPages' 키에는 조회 결과의 총 페이지 수를 할당합니다.
        Map<String, Object> responseData = orderDetailService.findBySeller(user, pageRequest);

        return ResponseEntity.ok(responseData);
    }

    @PatchMapping
    public ResponseEntity<?> orderStatusChange(@RequestBody OrderDetailStatusUpdateRequestDTO requestDTO, Principal principal) {
        User user = userAuthValidator.getCurrentUser(principal);
        OrderDetail orderDetail = getOrderDetail(requestDTO.getOrderDetailID());

        userAuthValidator.validateUserAccessOrderDetail(user, orderDetail);
        orderStatusUpdateService.updateOrderStatus(orderDetail, requestDTO.getOrderStatus());

        return ResponseEntity.ok(Map.of("message", "주문 처리 상태가 변경되었습니다. "));
    }

    @GetMapping("/{orderDetailID}")
    public ResponseEntity<?> orderDetailInfoForSeller(@PathVariable("orderDetailID") Long orderDetailID) {
        OrderDetail orderDetail = getOrderDetail(orderDetailID);

        OrderDetailResponseDTO orderDetailResponseDTO = new OrderDetailResponseDTO(orderDetail);
        OrderResponseDTO orderResponseDTO = new OrderResponseDTO(orderDetail.getOrder());
        PaymentResponseDTO paymentResponseDTO = new PaymentResponseDTO(paymentService.findByOrder(orderDetail.getOrder()));

        return ResponseEntity.ok(Map.of(
                "orderDetail", orderDetailResponseDTO,
                "order", orderResponseDTO,
                "payment", paymentResponseDTO
        ));
    }

    @GetMapping("/list")
    public ResponseEntity<?> list(Principal principal) {
        User user = userAuthValidator.getCurrentUser(principal);
        List<OrderDetailNameAndIdDTO> orderDetailsByUser = orderDetailService.findTop5RecentOrderDetailsByUser(user);
        return ResponseEntity.ok(orderDetailsByUser);
    }


    private OrderDetail getOrderDetail(Long orderDetailID) {
        return orderDetailService.findByID(orderDetailID)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "주문 정보가 존재하지 않습니다."));
    }
}
