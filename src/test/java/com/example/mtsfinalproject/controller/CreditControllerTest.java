package com.example.mtsfinalproject.controller;

import static org.mockito.Mockito.verify;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.mtsfinalproject.dto.LoanOrderCreateDto;
import com.example.mtsfinalproject.dto.LoanOrderDeleteDto;
import com.example.mtsfinalproject.dto.LoanOrderDto;
import com.example.mtsfinalproject.dto.TariffDto;
import com.example.mtsfinalproject.service.CreditService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
class CreditControllerTest {

    @MockBean
    private CreditService service;

    @Autowired
    private MockMvc mvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void getTariffs() throws Exception {
        Long tariffId = 100L;
        String type = "test";
        String interestRate = "20%";

        TariffDto tariffDto = new TariffDto();
        tariffDto.setId(tariffId).setType(type).setInterestRate(interestRate);

        when(service.getTariffs()).thenReturn(Arrays.asList(tariffDto));

        MvcResult mvcResult = mvc.perform(get("/loan-service/getTariffs").accept(MediaType.APPLICATION_JSON))
                .andReturn();

        mvc.perform(asyncDispatch(mvcResult))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.tariffs[0].id", equalTo(100)))
                .andExpect(jsonPath("$.data.tariffs[0].type", equalTo(type)))
                .andExpect(jsonPath("$.data.tariffs[0].interestRate", equalTo(interestRate)));

        verify(service).getTariffs();
    }



    @Test
    void createOrder() throws Exception {
        UUID uuid = UUID.randomUUID();
        Long userId = 100L;
        Long tariffId = 100L;
        LoanOrderCreateDto dto = new LoanOrderCreateDto();
        dto.setUserId(userId).setTariffId(tariffId);

        when(service.createOrder(dto)).thenReturn(new LoanOrderDto()
                .setOrderId(uuid)
                .setCreditRating(0.5)
                .setStatus("IN_PROGRESS")
                .setTimeInsert(new Timestamp(System.currentTimeMillis()))
                .setTimeInsert(new Timestamp(System.currentTimeMillis()))
                .setUserId(userId)
                .setTariffId(tariffId));

        MvcResult mvcResult = mvc.perform(post("/loan-service/order")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andReturn();

        mvc.perform(asyncDispatch(mvcResult))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.orderId", equalTo(uuid.toString())));

        verify(service).createOrder(dto);
    }

    @Test
    void getStatusOrder() throws Exception {
        UUID orderId = UUID.randomUUID();
        Long userId = 1L;
        Long tariffId = 1L;

        when(service.getStatusOrder(orderId)).thenReturn(new LoanOrderDto()
                .setOrderId(orderId)
                .setCreditRating(0.5)
                .setStatus("IN_PROGRESS")
                .setTimeInsert(new Timestamp(System.currentTimeMillis()))
                .setTimeInsert(new Timestamp(System.currentTimeMillis()))
                .setUserId(userId)
                .setTariffId(tariffId));

        MvcResult mvcResult = mvc.perform(get("/loan-service/getStatusOrder?orderId=" + orderId).accept(MediaType.APPLICATION_JSON))
                .andReturn();

        mvc.perform(asyncDispatch(mvcResult))
                .andDo(print())
                .andExpect(jsonPath("$.data.orderStatus", equalTo("IN_PROGRESS")));

        verify(service).getStatusOrder(orderId);
    }

    @Test
    void deleteOrder() throws Exception {
        UUID orderId = UUID.randomUUID();
        Long userId = 1L;

        LoanOrderDeleteDto dto = new LoanOrderDeleteDto();
        dto.setOrderId(orderId).setUserId(userId);

        mvc.perform(delete("/loan-service/deleteOrder").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isOk());

        verify(service).deleteOrder(dto);
    }
}