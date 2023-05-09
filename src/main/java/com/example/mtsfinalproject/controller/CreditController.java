package com.example.mtsfinalproject.controller;

import com.example.mtsfinalproject.dto.LoanOrderCreateDto;
import com.example.mtsfinalproject.dto.LoanOrderDeleteDto;
import com.example.mtsfinalproject.service.CreditService;
import com.example.mtsfinalproject.wrappers.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class CreditController {

    private final CreditService creditService;

    @GetMapping("getTariffs")
    public ResponseEntity<DataWrap> getTariffs() {
        return ResponseEntity.ok(new DataWrap(new TariffsWrap(creditService.getTariffs())));
    }

    @PostMapping("getUserOrders")
    public ResponseEntity<DataWrap> getUserOrders(@RequestBody Long userId){
        return ResponseEntity.ok(new DataWrap(new LoanOrdersWrap(creditService.getUserOrders(userId))));
    }

    @PostMapping("order")
    public ResponseEntity<DataWrap> createOrder(@RequestBody LoanOrderCreateDto dto) {
        return ResponseEntity.ok(new DataWrap(new OrderIdWrap(creditService.createOrder(dto).getOrderId())));
    }

    @GetMapping("getStatusOrder")
    public ResponseEntity<DataWrap> getStatusOrder(@RequestParam("orderId") UUID orderId) {
        return ResponseEntity.ok(new DataWrap(new OrderStatusWrap(creditService.getStatusOrder(orderId).getStatus())));
    }

    @DeleteMapping("deleteOrder")
    public ResponseEntity<Void> deleteOrder(@RequestBody LoanOrderDeleteDto dto) {
            creditService.deleteOrder(dto);
            return ResponseEntity.ok().build();
    }
}

