package com.bulkpurchase.web.controller.api;

import com.bulkpurchase.domain.dto.productInquiry.ProductInquiryCreateRequestDTO;
import com.bulkpurchase.domain.dto.productInquiry.ProductInquiryReplyRequestDTO;
import com.bulkpurchase.domain.dto.productInquiry.ProductInquiryResponseDTO;
import com.bulkpurchase.domain.dto.productInquiry.ProductInquiryUpdateRequestDTO;
import com.bulkpurchase.domain.entity.product.Product;
import com.bulkpurchase.domain.entity.product.ProductInquiry;
import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.service.product.ProductInquiryService;
import com.bulkpurchase.domain.service.product.ProductService;
import com.bulkpurchase.domain.validator.user.UserAuthValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/product-inquiry")
public class ProductInquiryAPIController {

    private final UserAuthValidator userAuthValidator;
    private final ProductInquiryService productInquiryService;
    private final ProductService productService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody @Validated ProductInquiryCreateRequestDTO requestDTO, Principal principal) {
        User user = userAuthValidator.getCurrentUser(principal);

        Product product = productService.findById(requestDTO.getProductID())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "상품이 존재하지 않습니다."));

        Long inquiryID = productInquiryService.save(new ProductInquiry(requestDTO, product, user)).getInquiryID();

        return ResponseEntity.ok(Map.of("message", "문의가 정상적으로 등록되었습니다.", "inquiryID", inquiryID));
    }

    @PatchMapping
    public ResponseEntity<?> update(@RequestBody ProductInquiryUpdateRequestDTO requestDTO,
                                    Principal principal) {

        if (requestDTO.getInquiryContent() == null || requestDTO.getInquiryID() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 요청입니다.");
        }

        ProductInquiry productInquiry = productInquiryService.findById(requestDTO.getInquiryID())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "문의가 존재하지 않습니다."));

        User user = userAuthValidator.getCurrentUser(principal);

        if (!productInquiry.getUser().equals(user)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "글을 수정할 권한이 없습니다.");
        }

        if (productInquiry.getReplyDate() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "답변이 달린 글은 수정할 수 없습니다.");
        }

        productInquiry.update(requestDTO);
        productInquiryService.save(productInquiry);

        return ResponseEntity.ok(Map.of("message", "문의 수정이 완료되었습니다.", "inquiryID", productInquiry.getInquiryID()));
    }

    @PatchMapping("/reply")
    public ResponseEntity<?> response(@RequestBody ProductInquiryReplyRequestDTO requestDTO,
                                      Principal principal) {
        if (requestDTO.getReplyContent() == null || requestDTO.getInquiryID() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 요청입니다.");
        }

        ProductInquiry productInquiry = productInquiryService.findById(requestDTO.getInquiryID())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "문의가 존재하지 않습니다."));

        User user = userAuthValidator.getCurrentUser(principal);

        if (!productInquiry.getProduct().getUser().equals(user)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "답변을 작성할 권한이 없습니다.");
        }

        if (productInquiry.getReplyDate() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 답변이 완료된 문의입니다.");
        }

        productInquiry.reply(requestDTO);
        productInquiryService.save(productInquiry);

        return ResponseEntity.ok(Map.of("message", "답변이 성공적으로 등록되었습니다.", "inquiryID", productInquiry.getInquiryID()));
    }

    @DeleteMapping("/{inquiryID}")
    public ResponseEntity<?> delete(@PathVariable("inquiryID") Long InquiryID, Principal principal) {
        ProductInquiry productInquiry = productInquiryService.findById(InquiryID)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "문의가 존재하지 않습니다."));

        User user = userAuthValidator.getCurrentUser(principal);
        if (!user.equals(productInquiry.getUser())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "문의를 삭제할 권한이 없습니다.");
        }

        if (productInquiry.getReplyContent() != null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "답변이 완료된 문의는 삭제할 수 없습니다.");
        }

        return ResponseEntity.ok(Map.of("message", "글 삭제가 완료되었습니다."));
    }

    @GetMapping("/{inquiryID}")
    public ResponseEntity<?> findOne(@PathVariable("inquiryID") Long InquiryID) {
        ProductInquiry productInquiry = productInquiryService.findById(InquiryID)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "문의가 존재하지 않습니다."));

        return ResponseEntity.ok(new ProductInquiryResponseDTO(productInquiry));
    }
    @GetMapping("/seller/{sellerID}")
    public ResponseEntity<?> findListBySeller(@PathVariable("sellerID") Long sellerID, @RequestParam(value = "page", defaultValue = "1") Integer page) {

        User user = userAuthValidator.getCurrentUserByUserID(sellerID);

        Sort sort = Sort.by(Sort.Direction.DESC, "inquiryID");
        Pageable pageable = PageRequest.of(page - 1, 10, sort);

        Page<ProductInquiryResponseDTO> inquiries = productInquiryService.findByProductUser(user, pageable);
        List<ProductInquiryResponseDTO> dtoList = inquiries.getContent();
        int totalPages = inquiries.getTotalPages();
        return ResponseEntity.ok(Map.of("inquiries", dtoList, "totalPages", totalPages));
    }
    @GetMapping("/product/{productID}")
    public ResponseEntity<?> findListByProduct(@PathVariable("productID") Long productID, @RequestParam(value = "page", defaultValue = "1") Integer page) {

        Product product = productService.findById(productID)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 상품입니다."));
        Sort sort = Sort.by(Sort.Direction.DESC, "inquiryID");
        Pageable pageable = PageRequest.of(page - 1, 10, sort);

        Page<ProductInquiryResponseDTO> inquiries = productInquiryService.findByProduct(product, pageable);
        List<ProductInquiryResponseDTO> dtoList = inquiries.getContent();
        int totalPages = inquiries.getTotalPages();
        return ResponseEntity.ok(Map.of("inquiries", dtoList, "totalPages", totalPages));
    }

}
