package com.bulkpurchase.web.controller.api;

import com.bulkpurchase.domain.dto.inquiry.InquiryCreateRequestDTO;
import com.bulkpurchase.domain.dto.inquiry.InquiryDetailResponseDTO;
import com.bulkpurchase.domain.dto.inquiry.InquiryReplyDTO;
import com.bulkpurchase.domain.entity.Inquiry;
import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.service.InquiryService;
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
@RequestMapping("/api/inquiry")
public class InquiryAPIController {

    private final InquiryService inquiryService;
    private final UserAuthValidator userAuthValidator;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody InquiryCreateRequestDTO inquiryCreateRequestDTO, Principal principal) {
        if (inquiryCreateRequestDTO.getInquiryContent() == null ||
                inquiryCreateRequestDTO.getTitle() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "필수 입력 필드가 누락되었습니다."));
        }

        User user = userAuthValidator.getCurrentUser(principal);

        Inquiry inquiry = new Inquiry();

        inquiry.setTitle(inquiryCreateRequestDTO.getTitle());
        inquiry.setInquiryContent(inquiryCreateRequestDTO.getInquiryContent());
        inquiry.setType(inquiryCreateRequestDTO.getInquiryType());
        inquiry.setUser(user);

        Inquiry save = inquiryService.save(inquiry);

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "문의가 성공적으로 작성되었습니다."));
    }

    @PostMapping("/response")
    public ResponseEntity<?> inquiryReply(@RequestBody InquiryReplyDTO inquiryReplyDTO) {
        Optional<Inquiry> inquiryOpt = inquiryService.findById(inquiryReplyDTO.getInquiryID());
        if (inquiryOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "존재하지 않는 문의입니다."));
        }
        Inquiry inquiry = inquiryOpt.get();
        inquiry.setReply(inquiryReplyDTO);
        inquiryService.save(inquiry);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("message", "답변 완료되었습니다."));
    }

    @GetMapping("/list")
    public ResponseEntity<?> list(Principal principal) {
        User user = userAuthValidator.getCurrentUser(principal);
        List<InquiryDetailResponseDTO> list = inquiryService.findByUser(user).stream().map(InquiryDetailResponseDTO::new).toList();
        return ResponseEntity.status(HttpStatus.OK).body(list);
    }

    @GetMapping("/{inquiryID}")
    public ResponseEntity<?> item(Principal principal, @PathVariable("inquiryID") Long inquiryID) {
        User user = userAuthValidator.getCurrentUser(principal);
        Optional<Inquiry> inquiryOpt = inquiryService.findByUserAndInquiryID(user, inquiryID);
        if (inquiryOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "문의가 존재하지 않습니다."));
        }

        InquiryDetailResponseDTO inquiryDetailResponseDTO = new InquiryDetailResponseDTO(inquiryOpt.get());
        return ResponseEntity.ok(inquiryDetailResponseDTO);
    }
}
