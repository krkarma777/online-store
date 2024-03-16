package com.bulkpurchase.web.controller.api;

import com.bulkpurchase.config.AppConfig;
import com.bulkpurchase.domain.dto.inquiry.InquiryCreateRequestDTO;
import com.bulkpurchase.domain.dto.inquiry.InquiryReplyDTO;
import com.bulkpurchase.domain.entity.Inquiry;
import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.enums.inquiry.InquiryType;
import com.bulkpurchase.domain.service.InquiryService;
import com.bulkpurchase.domain.service.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class InquiryAPIControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private InquiryService inquiryService;
    @Autowired
    private UserService userService;
    private final ObjectMapper objectMapper = new AppConfig().objectMapper();
    private Inquiry inquiry = new Inquiry();

    @BeforeEach
    public void setUp() {
        inquiry.setTitle("Pre-set Title");
        inquiry.setInquiryContent("Pre-set content");
        User user = userService.findByUsername("zxczxc").orElse(null);
        inquiry.setType(InquiryType.GENERAL);
        inquiry.setUser(user);
        inquiry = inquiryService.save(inquiry);
    }


    @Test
    @WithMockUser(username="zxczxc", roles={"자영업자"})
    public void whenPostInquiry_thenCreateInquiry() throws Exception {
        InquiryCreateRequestDTO inquiryDTO = new InquiryCreateRequestDTO();
        inquiryDTO.setTitle("Test Inquiry");
        inquiryDTO.setInquiryContent("This is a test content");
        inquiryDTO.setInquiryType(InquiryType.GENERAL);

        mockMvc.perform(post("/api/inquiry")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inquiryDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("문의가 성공적으로 작성되었습니다."));
    }

    @Test
    @WithMockUser(username="werwer", roles={"관리자"})
    public void whenReplyToInquiry_thenMarkInquiryAsAnswered() throws Exception {
        InquiryReplyDTO replyDTO = new InquiryReplyDTO();
        replyDTO.setInquiryID(inquiry.getInquiryID());
        replyDTO.setResponseContent("This is a response to the inquiry");
        System.out.println("replyDTO = " + replyDTO);
        mockMvc.perform(post("/api/inquiry/response-inquiry")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(replyDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("답변 완료되었습니다."));
    }

    @Test
    @WithMockUser(username="zxczxc", roles={"자영업자"})
    public void whenListUserInquiries_thenReturnListOfInquiries() throws Exception {
        mockMvc.perform(get("/api/inquiry/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThan(0))))
                .andExpect(jsonPath("$[0].title").exists());
    }

    @Test
    @WithMockUser(username="zxczxc", roles={"자영업자"})
    public void whenViewItem_givenInquiryID_thenInquiryDetails() throws Exception {
        long inquiryID = inquiry.getInquiryID();

        mockMvc.perform(get("/api/inquiry").param("inquiryID", Long.toString(inquiryID)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").exists())
                .andExpect(jsonPath("$.inquiryContent").exists());
    }

    @Test
    @WithMockUser(username="zxczxc", roles={"자영업자"})
    public void whenViewItemWithNonExistingInquiryID_thenNotFound() throws Exception {
        long nonExistingInquiryID = 9999L; // 존재하지 않는 ID 가정

        mockMvc.perform(get("/api/inquiry").param("inquiryID", Long.toString(nonExistingInquiryID)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("문의가 존재하지 않습니다."));
    }

    @Test
    @WithMockUser(username="zxczxc", roles={"자영업자"})
    public void whenPostInquiryWithMissingFields_thenBadRequest() throws Exception {
        InquiryCreateRequestDTO inquiryDTO = new InquiryCreateRequestDTO();
        // title 필드를 의도적으로 누락
        inquiryDTO.setInquiryContent("This is a test content without a title");

        mockMvc.perform(post("/api/inquiry")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inquiryDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("필수 입력 필드가 누락되었습니다."));
    }
}
