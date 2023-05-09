package com.example.mtsfinalproject.service;

import com.example.mtsfinalproject.dto.LoanOrderCreateDto;
import com.example.mtsfinalproject.dto.LoanOrderDeleteDto;
import com.example.mtsfinalproject.dto.LoanOrderDto;
import com.example.mtsfinalproject.dto.TariffDto;

import java.util.List;
import java.util.UUID;

public interface CreditService {

    List<TariffDto> getTariffs();

    List<LoanOrderDto> getUserOrders(Long userId);

    LoanOrderDto createOrder(LoanOrderCreateDto dto);

    LoanOrderDto getStatusOrder(UUID orderId);

    void deleteOrder(LoanOrderDeleteDto dto);


}
