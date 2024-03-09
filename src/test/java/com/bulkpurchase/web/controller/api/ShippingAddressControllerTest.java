package com.bulkpurchase.web.controller.api;

import com.bulkpurchase.domain.dto.user.ShippingAddressDTO;
import com.bulkpurchase.domain.entity.user.ShippingAddress;
import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.service.user.ShippingAddressService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ShippingAddressControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ShippingAddressService shippingAddressService;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ShippingAddressDTO shippingAddressDTO = new ShippingAddressDTO();

    @BeforeEach
    void setUp() {
        shippingAddressDTO.setAddress("서울시 동작구");
        shippingAddressDTO.setDetailAddress("313호");
        shippingAddressDTO.setZipCode("12345");
        shippingAddressDTO.setRecipientName("홍길동");
        shippingAddressDTO.setPhoneNumber("010-0000-0000");
    }

    @Test
    @WithMockUser(username="zxczxc", roles={"자영업자"})
    void testShippingAddressListForm() throws Exception {
        User user = new User();
        when(shippingAddressService.findByUser(user)).thenReturn(Arrays.asList(new ShippingAddress(), new ShippingAddress()));

        mockMvc.perform(get("/api/shipping-address"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(username="zxczxc", roles={"자영업자"})
    void testCreateShippingAddress() throws Exception {
        when(shippingAddressService.findByUser(ArgumentMatchers.any(User.class))).thenReturn(Arrays.asList());
        doNothing().when(shippingAddressService).save(ArgumentMatchers.any(ShippingAddress.class));

        mockMvc.perform(post("/api/shipping-address")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(shippingAddressDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("배송지 추가에 성공하셨습니다."));
    }

    @Test
    @WithMockUser(username="zxczxc", roles={"자영업자"})
    void testUpdateShippingAddress() throws Exception {
        shippingAddressDTO.setId(1L);
        shippingAddressDTO.setAddress("test 변경");

        when(shippingAddressService.findByUserAndId(ArgumentMatchers.any(User.class), ArgumentMatchers.anyLong())).thenReturn(Optional.of(new ShippingAddress()));
        doNothing().when(shippingAddressService).save(ArgumentMatchers.any(ShippingAddress.class));

        mockMvc.perform(patch("/api/shipping-address")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(shippingAddressDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("배송지 수정에 성공하셨습니다."));
    }

    @Test
    @WithMockUser(username="zxczxc", roles={"자영업자"})
    void testDeleteShippingAddress() throws Exception {
        long addressId = 1L;
        when(shippingAddressService.findByUserAndId(ArgumentMatchers.any(User.class), ArgumentMatchers.eq(addressId))).thenReturn(Optional.of(new ShippingAddress()));
        doNothing().when(shippingAddressService).delete(addressId);

        mockMvc.perform(delete("/api/shipping-address")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addressId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("삭제에 성공하셨습니다."));
    }
}