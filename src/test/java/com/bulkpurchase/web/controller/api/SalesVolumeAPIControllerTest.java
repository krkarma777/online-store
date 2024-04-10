package com.bulkpurchase.web.controller.api;

import com.bulkpurchase.domain.dto.seller.SalesDataDTO;
import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.enums.UserRole;
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
import org.springframework.security.test.context.support.WithMockUser;
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
        user.setRole(UserRole.ROLE_판매자);
        System.out.println("user = " + user);
        when(userAuthValidator.getCurrentUser(any(Principal.class))).thenReturn(user);
    }

    @Test
    @WithMockUser
    public void testFindSalesDataForLast30Days() throws Exception {
        Principal mockPrincipal = mock(Principal.class);
        when(mockPrincipal.getName()).thenReturn("testUser");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        List<SalesDataDTO> salesDataDTOList = new ArrayList<>();
        // 예시 데이터 추가
        salesDataDTOList.add(new SalesDataDTO(formatter.format(startDate),  new BigDecimal("1000")));
        salesDataDTOList.add(new SalesDataDTO(formatter.format(startDate.plusDays(1)),  new BigDecimal("1500")));

        when(orderService.calculateSalesLast30DaysBySeller(eq(user.getUserID()), eq(startDate), eq(endDate))).thenReturn(salesDataDTOList);

        mockMvc.perform(get("/api/sales-volume/last-30-days")
                        .principal(mockPrincipal)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(salesDataDTOList)));

        verify(orderService, times(1)).calculateSalesLast30DaysBySeller(eq(user.getUserID()), eq(startDate), eq(endDate));
    }

    // 지난 12개월 판매 데이터 테스트
    @Test
    @WithMockUser
    public void testFindSalesDataOverLastYear() throws Exception {
        Principal mockPrincipal = mock(Principal.class);
        when(mockPrincipal.getName()).thenReturn("testUser");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        List<SalesDataDTO> salesDataDTOList = new ArrayList<>();
        // 예시 데이터 추가
        salesDataDTOList.add(new SalesDataDTO(formatter.format(startDate.minusMonths(11)), new BigDecimal("2000")));
        salesDataDTOList.add(new SalesDataDTO(formatter.format(startDate.minusMonths(10)), new BigDecimal("2500")));

        when(userAuthValidator.getCurrentUser(any(Principal.class))).thenReturn(user);
        when(orderService.calculateSalesLast12MonthsBySeller(eq(user.getUserID()))).thenReturn(salesDataDTOList);

        mockMvc.perform(get("/api/sales-volume/last-year")
                        .principal(mockPrincipal)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(salesDataDTOList)));

        verify(orderService, times(1)).calculateSalesLast12MonthsBySeller(eq(user.getUserID()));
    }

    // 최근 3년 판매 데이터 테스트
    @Test
    @WithMockUser
    public void testFindSalesDataOverLastThreeYears() throws Exception {
        Principal mockPrincipal = mock(Principal.class);
        when(mockPrincipal.getName()).thenReturn("testUser");

        List<SalesDataDTO> salesDataDTOList = new ArrayList<>();
        // 예시 데이터 추가
        salesDataDTOList.add(new SalesDataDTO("2019", new BigDecimal("12000")));
        salesDataDTOList.add(new SalesDataDTO("2020", new BigDecimal("15000")));
        salesDataDTOList.add(new SalesDataDTO("2021", new BigDecimal("18000")));

        when(userAuthValidator.getCurrentUser(any(Principal.class))).thenReturn(user);
        when(orderService.calculateSalesLast3YearsBySeller(eq(user.getUserID()))).thenReturn(salesDataDTOList);

        mockMvc.perform(get("/api/sales-volume/last-three-years")
                        .principal(mockPrincipal)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(salesDataDTOList)));

        verify(orderService, times(1)).calculateSalesLast3YearsBySeller(eq(user.getUserID()));
    }
}
