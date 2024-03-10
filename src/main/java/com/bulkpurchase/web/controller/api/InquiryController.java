package com.bulkpurchase.web.controller.api;

import com.bulkpurchase.domain.dto.inquiry.InquiryCreateRequestDTO;
import com.bulkpurchase.domain.dto.inquiry.InquiryDetailResponseDTO;
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
public class InquiryController {

    private final InquiryService inquiryService;
    private final UserAuthValidator userAuthValidator;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody InquiryCreateRequestDTO inquiryCreateRequestDTO, Principal principal) {
        User user = userAuthValidator.getCurrentUser(principal);

        Inquiry inquiry = new Inquiry();

        inquiry.setTitle(inquiryCreateRequestDTO.getTitle());
        inquiry.setInquiryContent(inquiryCreateRequestDTO.getInquiryContent());
        inquiry.setType(inquiryCreateRequestDTO.getInquiryType());
        inquiry.setUser(user);

        Inquiry save = inquiryService.save(inquiry);

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "문의가 성공적으로 작성되었습니다."));
    }

    @GetMapping("/list")
    public ResponseEntity<?> listView(Principal principal) {
        User user = userAuthValidator.getCurrentUser(principal);
        List<Inquiry> inquiries = inquiryService.findByUser(user);
        List<InquiryDetailResponseDTO> dtoList = new ArrayList<>();
        for (Inquiry inquiry : inquiries) {
            InquiryDetailResponseDTO inquiryDetailResponseDTO = new InquiryDetailResponseDTO(inquiry);
            dtoList.add(inquiryDetailResponseDTO);
        }
        return ResponseEntity.status(HttpStatus.OK).body(dtoList);
    }

    @GetMapping
    public ResponseEntity<?> itemView(Principal principal, @RequestParam("inquiryID") Long inquiryID) {
        User user = userAuthValidator.getCurrentUser(principal);
        Optional<Inquiry> inquiryOpt = inquiryService.findByUserAndInquiryID(user, inquiryID);
        if (inquiryOpt.isPresent()) {
            Inquiry inquiry = inquiryOpt.get();
            InquiryDetailResponseDTO inquiryDetailResponseDTO = new InquiryDetailResponseDTO(inquiry);
            return ResponseEntity.ok(inquiryDetailResponseDTO);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "비정상적인 요청입니다."));
        }
    }
}
