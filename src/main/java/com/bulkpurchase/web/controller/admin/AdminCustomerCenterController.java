package com.bulkpurchase.web.controller.admin;

import com.bulkpurchase.domain.entity.Inquiry;
import com.bulkpurchase.domain.service.InquiryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/manage/cs")
public class AdminCustomerCenterController {

    private final InquiryService inquiryService;

    @GetMapping("/inquiry")
    public String inquiryManage(Model model, @RequestParam(value = "page",required = false) Integer page) {
        if (page == null) {
            page = 1;
        }
        int pageSize = 10;

        Sort sort = Sort.by(Sort.Direction.fromString("DESC"), "inquiryID");
        Pageable pageable = PageRequest.of(page-1, pageSize, sort);
        Page<Inquiry> inquiryList = inquiryService.findAll(pageable);
        model.addAttribute("inquiryList", inquiryList);
        return "admin/InquiryManage";
    }
}
