package com.example.mtsfinalproject.service.impl;

import com.example.mtsfinalproject.constants.ErrorEnum;
import com.example.mtsfinalproject.dto.LoanOrderCreateDto;
import com.example.mtsfinalproject.dto.LoanOrderDeleteDto;
import com.example.mtsfinalproject.dto.LoanOrderDto;
import com.example.mtsfinalproject.dto.TariffDto;
import com.example.mtsfinalproject.entity.LoanOrderEntity;
import com.example.mtsfinalproject.entity.TariffEntity;
import com.example.mtsfinalproject.mapper.LoanOrderMapper;
import com.example.mtsfinalproject.mapper.TariffMapper;
import com.example.mtsfinalproject.repository.LoanOrderRepository;
import com.example.mtsfinalproject.repository.TariffRepository;
import com.example.mtsfinalproject.service.CreditService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreditServiceImpl implements CreditService {

    private final TariffRepository tariffRepository;
    private final LoanOrderRepository loanOrderRepository;

    private final TariffMapper tariffMapper;
    private final LoanOrderMapper loanOrderMapper;

    @Override
    public List<TariffDto> getTariffs() {
        List<TariffDto> tariffDtos = new ArrayList<>();
        for (TariffEntity tariff : tariffRepository.findAll()){
            tariffDtos.add(tariffMapper.toDto(tariff));
        }
        return tariffDtos;
    }

    @Override
    public List<LoanOrderDto> getUserOrders(Long userId){
        List<LoanOrderDto> loanOrderDtos = new ArrayList<>();
        for (LoanOrderEntity loanOrder : loanOrderRepository.findAll(userId)){
            loanOrderDtos.add(loanOrderMapper.toDto(loanOrder));
        }
        return loanOrderDtos;
    }

    @Override
    public LoanOrderDto createOrder(LoanOrderCreateDto dto) {
        if (!tariffRepository.isExist(dto.getTariffId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorEnum.TARIFF_NOT_FOUND.name());
        }

        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());

        List<LoanOrderEntity> listOfOrders = loanOrderRepository.findAll(dto.getUserId());
        for (LoanOrderEntity order : listOfOrders) {
            if (dto.getTariffId() == order.getTariffId()) {
                switch (order.getStatus()) {
                    case "IN_PROGRESS":
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorEnum.LOAN_CONSIDERATION.name());
                    case "APPROVED":
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorEnum.LOAN_ALREADY_APPROVED.name());
                    case "REFUSED":
                        if (currentTimestamp.getTime() - order.getTimeUpdate().getTime() < 120000) {
                            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorEnum.TRY_LATER.name());
                        }
                        break;

                }
            }
        }

        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
        symbols.setDecimalSeparator('.');

        LoanOrderEntity loanOrderEntity = new LoanOrderEntity();
        loanOrderEntity.setOrderId(UUID.randomUUID())
                .setUserId(dto.getUserId())
                .setTariffId(dto.getTariffId())
                .setCreditRating(Double.parseDouble(new DecimalFormat("#.##", symbols).format(0.1 + Math.random() * 0.8)))
                .setStatus("IN_PROGRESS")
                .setTimeInsert(currentTimestamp)
                .setTimeUpdate(currentTimestamp);

        loanOrderRepository.save(loanOrderEntity);

        return loanOrderMapper.toDto(loanOrderEntity);
    }

    @Override
    public LoanOrderDto getStatusOrder(UUID orderId) {
        if (!loanOrderRepository.isExist(orderId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorEnum.ORDER_NOT_FOUND.name());
        }
        return loanOrderMapper.toDto(loanOrderRepository.getOrder(orderId));
    }

    @Override
    public void deleteOrder(LoanOrderDeleteDto dto) {
        if (!loanOrderRepository.isExist(dto.getUserId(), dto.getOrderId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorEnum.ORDER_IMPOSSIBLE_TO_DELETE.name());
        }
        loanOrderRepository.deleteOrder(dto.getUserId(), dto.getOrderId());
    }

}
