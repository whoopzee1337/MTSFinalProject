package com.example.mtsfinalproject.service.impl;

import com.example.mtsfinalproject.constants.ErrorEnum;
import com.example.mtsfinalproject.dto.LoanOrderCreateDto;
import com.example.mtsfinalproject.dto.LoanOrderDeleteDto;
import com.example.mtsfinalproject.dto.LoanOrderDto;
import com.example.mtsfinalproject.dto.TariffDto;
import com.example.mtsfinalproject.entity.LoanOrderEntity;
import com.example.mtsfinalproject.entity.TariffEntity;
import com.example.mtsfinalproject.mapper.LoanOrderMapper;
import com.example.mtsfinalproject.mapper.LoanOrderMapperImpl;
import com.example.mtsfinalproject.mapper.TariffMapper;
import com.example.mtsfinalproject.mapper.TariffMapperImpl;
import com.example.mtsfinalproject.repository.LoanOrderRepository;
import com.example.mtsfinalproject.repository.TariffRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreditServiceImplTest {

    @Mock
    LoanOrderRepository loanOrderRepository;

    @Spy
    TariffMapper tariffMapper = new TariffMapperImpl();

    @Spy
    LoanOrderMapper loanOrderMapper = new LoanOrderMapperImpl();

    @Mock
    TariffRepository tariffRepository;

    @InjectMocks
    CreditServiceImpl creditService;

    @Test
    void testGetTariffs() {
        List<TariffEntity> tariffEntities = Arrays.asList(
                new TariffEntity().setId(1L).setType("Tariff1").setInterestRate("10%"),
                new TariffEntity().setId(2L).setType("Tariff2").setInterestRate("5%"),
                new TariffEntity().setId(3L).setType("Tariff3").setInterestRate("20%")
        );

        List<TariffDto> expectedTariffDtos = Arrays.asList(
                new TariffDto().setId(1L).setType("Tariff1").setInterestRate("10%"),
                new TariffDto().setId(2L).setType("Tariff2").setInterestRate("5%"),
                new TariffDto().setId(3L).setType("Tariff3").setInterestRate("20%")
        );

        when(tariffRepository.findAll()).thenReturn(tariffEntities);

        List<TariffDto> actualTariffDtos = creditService.getTariffs();

        assertEquals(expectedTariffDtos, actualTariffDtos);
    }

    @Test
    void testGetUserOrders(){
        UUID uuid = UUID.randomUUID();
        List<LoanOrderEntity> loanOrderEntities = Arrays.asList(
                new LoanOrderEntity()
                        .setOrderId(uuid)
                        .setUserId(1L)
                        .setCreditRating(0.55)
                        .setStatus("test")
                        .setTariffId(1L)
        );
        List<LoanOrderDto> expectedLoanOrderDtos = Arrays.asList(
                new LoanOrderDto()
                        .setOrderId(uuid)
                        .setUserId(1L)
                        .setCreditRating(0.55)
                        .setStatus("test")
                        .setTariffId(1L)
        );
        when(loanOrderRepository.findAll(1L)).thenReturn(loanOrderEntities);

        List<LoanOrderDto> actualTariffDtos = creditService.getUserOrders(1L);

        assertEquals(actualTariffDtos, expectedLoanOrderDtos);
    }


    @Test
    public void testCreateOrderSuccess() {
        Long userId = 1L;
        Long tariffId = 1L;
        LoanOrderCreateDto dto = new LoanOrderCreateDto();
        dto.setUserId(userId).setTariffId(tariffId);
        when(tariffRepository.isExist(tariffId)).thenReturn(true);
        when(loanOrderRepository.findAll(userId)).thenReturn(new ArrayList<>());

        LoanOrderDto result = creditService.createOrder(dto);

        verify(tariffRepository).isExist(tariffId);
        verify(loanOrderRepository).findAll(userId);
        verify(loanOrderRepository).save(Mockito.any());

        assertNotNull(result);
        assertEquals(userId, result.getUserId());
        assertEquals(tariffId, result.getTariffId());
        assertEquals("IN_PROGRESS", result.getStatus());
        assertNotNull(result.getOrderId());
        assertNotNull(result.getTimeInsert());
        assertNotNull(result.getTimeUpdate());
    }

    @Test
    public void testCreateOrderWithNonExistingTariff() {
        Long userId = 1L;
        Long tariffId = 1L;

        LoanOrderCreateDto dto = new LoanOrderCreateDto();
        dto.setUserId(userId).setTariffId(tariffId);

        when(tariffRepository.isExist(tariffId)).thenReturn(false);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> creditService.createOrder(dto));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals(ErrorEnum.TARIFF_NOT_FOUND.name(), exception.getReason());

        verify(tariffRepository).isExist(tariffId);
    }

    @Test
    public void testCreateOrderWithLoanConsideration() {
        Long userId = 1L;
        Long tariffId = 1L;

        LoanOrderCreateDto dto = new LoanOrderCreateDto();
        dto.setUserId(userId).setTariffId(tariffId);

        List<LoanOrderEntity> existingOrders = new ArrayList<>();
        LoanOrderEntity existingOrder = new LoanOrderEntity();
        existingOrder.setStatus("IN_PROGRESS").setTariffId(tariffId);

        existingOrders.add(existingOrder);

        when(tariffRepository.isExist(tariffId)).thenReturn(true);
        when(loanOrderRepository.findAll(userId)).thenReturn(existingOrders);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> creditService.createOrder(dto));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals(ErrorEnum.LOAN_CONSIDERATION.name(), exception.getReason());

        verify(tariffRepository).isExist(tariffId);
        verify(loanOrderRepository).findAll(userId);
    }

    @Test
    public void testCreateOrderWithLoanAlreadyApproved() {
        Long userId = 1L;
        Long tariffId = 1L;

        LoanOrderCreateDto dto = new LoanOrderCreateDto();
        dto.setUserId(userId).setTariffId(tariffId);

        List<LoanOrderEntity> existingOrders = new ArrayList<>();
        LoanOrderEntity existingOrder = new LoanOrderEntity();
        existingOrder.setStatus("APPROVED").setTariffId(tariffId);
        existingOrder.setTariffId(tariffId);

        existingOrders.add(existingOrder);

        when(tariffRepository.isExist(tariffId)).thenReturn(true);
        when(loanOrderRepository.findAll(userId)).thenReturn(existingOrders);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> creditService.createOrder(dto));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals(ErrorEnum.LOAN_ALREADY_APPROVED.name(), exception.getReason());

        verify(tariffRepository).isExist(tariffId);
        verify(loanOrderRepository).findAll(userId);
    }

    @Test
    public void testCreateOrderWithLoanTryLater() {
        Long userId = 1L;
        Long tariffId = 1L;
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());

        LoanOrderCreateDto dto = new LoanOrderCreateDto();
        dto.setUserId(userId).setTariffId(tariffId);

        List<LoanOrderEntity> existingOrders = new ArrayList<>();
        LoanOrderEntity existingOrder = new LoanOrderEntity();
        existingOrder.setStatus("REFUSED").setTariffId(tariffId).setTimeUpdate(currentTimestamp);

        existingOrders.add(existingOrder);

        when(tariffRepository.isExist(tariffId)).thenReturn(true);
        when(loanOrderRepository.findAll(userId)).thenReturn(existingOrders);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> creditService.createOrder(dto));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals(ErrorEnum.TRY_LATER.name(), exception.getReason());

        verify(tariffRepository).isExist(tariffId);
        verify(loanOrderRepository).findAll(userId);
    }

    @Test
    void testGetStatusOrderSuccess() {
        UUID orderId = UUID.randomUUID();
        LoanOrderEntity existingOrder = new LoanOrderEntity();
        existingOrder.setOrderId(orderId);

        when(loanOrderRepository.isExist(orderId)).thenReturn(true);
        when(loanOrderRepository.getOrder(orderId)).thenReturn(existingOrder);

        LoanOrderDto result = creditService.getStatusOrder(orderId);

        assertNotNull(result);
        assertEquals(orderId, result.getOrderId());

        verify(loanOrderRepository).isExist(orderId);
        verify(loanOrderRepository).getOrder(orderId);
    }

    @Test
    public void testGetStatusOrderNotFound() {
        UUID orderId = UUID.randomUUID();

        when(loanOrderRepository.isExist(orderId)).thenReturn(false);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> creditService.getStatusOrder(orderId));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals(ErrorEnum.ORDER_NOT_FOUND.name(), exception.getReason());

        verify(loanOrderRepository).isExist(orderId);
    }


    @Test
    public void testDeleteOrderSuccess() {
        Long userId = 1L;
        UUID orderId = UUID.randomUUID();

        LoanOrderDeleteDto dto = new LoanOrderDeleteDto();
        dto.setUserId(userId).setOrderId(orderId);

        when(loanOrderRepository.isExist(userId, orderId)).thenReturn(true);

        creditService.deleteOrder(dto);

        verify(loanOrderRepository).isExist(userId, orderId);
        verify(loanOrderRepository).deleteOrder(userId, orderId);
    }

    @Test
    public void testDeleteOrderImpossibleToDelete() {
        Long userId = 1L;
        UUID orderId = UUID.randomUUID();

        LoanOrderDeleteDto dto = new LoanOrderDeleteDto();
        dto.setUserId(userId).setOrderId(orderId);

        when(loanOrderRepository.isExist(userId, orderId)).thenReturn(false);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> creditService.deleteOrder(dto));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals(ErrorEnum.ORDER_IMPOSSIBLE_TO_DELETE.name(), exception.getReason());

        verify(loanOrderRepository).isExist(userId, orderId);
    }
}