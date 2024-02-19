package com.bulkpurchase.web.controller.seller;

import com.bulkpurchase.domain.dto.ProductInquiryDTO;
import com.bulkpurchase.domain.dto.ReplyRequestDTO;
import com.bulkpurchase.domain.entity.product.ProductInquiry;
import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.service.product.ProductInquiryService;
import com.bulkpurchase.domain.service.user.UserService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class InquiryManageController {

    private final UserService userService;
    private final ProductInquiryService productInquiryService;

    @GetMapping("/seller/inquiries")
    public String inquiriesManage(Principal principal, Model model) {
        User user = userService.findByUsername(principal.getName());
        List<ProductInquiry> inquiries = productInquiryService.findByProductUser(user);
        model.addAttribute("inquiries", inquiries);
        return "/seller/inquiries";
    }

    @PostMapping("/inquiries/reply")
    public ResponseEntity<?> inquiryReply(@RequestBody ReplyRequestDTO replyRequest) {
        try {
            ProductInquiry inquiry = productInquiryService.replyToInquiry(replyRequest.getInquiryID(), replyRequest.getReplyContent());
            return ResponseEntity.ok().body("답변이 성공적으로 등록되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("답변 등록에 실패하였습니다.");
        }
    }

    @GetMapping("/inquiries/load-more")
    public ResponseEntity<List<ProductInquiryDTO>> loadMoreInquiries(@RequestParam(value = "page") int page, @RequestParam(value = "size") int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<ProductInquiryDTO> inquiriesPage = productInquiryService.findAllDTO(pageable);
        return ResponseEntity.ok(inquiriesPage.getContent());
    }

}
