package com.bulkpurchase.web.controller.api;

import com.bulkpurchase.domain.dto.seller.SalesDataDTO;
import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.service.order.OrderService;
import com.bulkpurchase.domain.validator.user.UserAuthValidator;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class SalesVolumeAPIControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserAuthValidator userAuthValidator;

    @MockBean
    private OrderService orderService;

    private LocalDate endDate;
    private LocalDate startDate;

    private User user;

    @BeforeEach
    public void setup() {
        endDate = LocalDate.now().plusDays(1);
        startDate = endDate.minusDays(30);

        user = new User();
        user.setUserID(1L);
    }

    @Test
    public void testFindSalesDataForLast30Days() throws Exception {
        Principal mockPrincipal = mock(Principal.class);
        when(mockPrincipal.getName()).thenReturn("testUser");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        List<SalesDataDTO> salesDataDTOList = new ArrayList<>();
        // 예시 데이터 추가
        salesDataDTOList.add(new SalesDataDTO(formatter.format(startDate),  new BigDecimal("1000")));
        salesDataDTOList.add(new SalesDataDTO(formatter.format(startDate.plusDays(1)),  new BigDecimal("1500")));

        when(userAuthValidator.getCurrentUser(any(Principal.class))).thenReturn(user);
        when(orderService.calculateSalesLast30DaysBySeller(eq(user.getUserID()), eq(startDate), eq(endDate))).thenReturn(salesDataDTOList);

        mockMvc.perform(get("/api/sales-volume")
                        .principal(mockPrincipal)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(salesDataDTOList)));

        verify(orderService, times(1)).calculateSalesLast30DaysBySeller(eq(user.getUserID()), eq(startDate), eq(endDate));
    }

}