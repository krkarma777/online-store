package com.bulkpurchase.web.controller.api;

import com.bulkpurchase.domain.entity.Inquiry;
import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.service.InquiryService;
import com.bulkpurchase.domain.validator.user.UserAuthValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/inquiry")
public class InquiryController {

    private final InquiryService inquiryService;
    private final UserAuthValidator userAuthValidator;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Inquiry inquiry, Principal principal) {
        User user = userAuthValidator.getCurrentUser(principal);
        inquiry.setUser(user);
        Inquiry save = inquiryService.save(inquiry);
        return ResponseEntity.status(HttpStatus.CREATED).body(save);
    }
}
