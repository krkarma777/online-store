package com.bulkpurchase.web.controller.api;

import com.bulkpurchase.domain.dto.inquiry.InquiryCreateRequestDTO;
import com.bulkpurchase.domain.enums.inquiry.InquiryType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class InquiryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username="zxczxc", roles={"자영업자"})
    public void whenPostInquiry_thenCreateInquiry() throws Exception {
        InquiryCreateRequestDTO inquiryDTO = new InquiryCreateRequestDTO();
        inquiryDTO.setTitle("Test Inquiry");
        inquiryDTO.setInquiryContent("This is a test content");
        inquiryDTO.setInquiryType(InquiryType.GENERAL);

        mockMvc.perform(post("/api/inquiry")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(inquiryDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("문의가 성공적으로 작성되었습니다."));
    }
}
