package com.example.mtsfinalproject.controller;


import com.example.mtsfinalproject.dto.LoanOrderDeleteDto;
import com.example.mtsfinalproject.wrappers.DataWrap;
import com.example.mtsfinalproject.wrappers.OrderIdWrap;
import com.example.mtsfinalproject.wrappers.OrderStatusWrap;
import com.example.mtsfinalproject.wrappers.TariffsWrap;
import com.example.mtsfinalproject.dto.LoanOrderCreateDto;
import com.example.mtsfinalproject.service.CreditService;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequiredArgsConstructor
@RequestMapping("loan-service")
public class CreditController {

    private final CreditService creditService;

    @GetMapping("getTariffs")
    @TimeLimiter(name = "oneSecTimeLimiter")
    public CompletableFuture<ResponseEntity<DataWrap>> getTariffs() {
        return CompletableFuture.supplyAsync(() -> ResponseEntity.ok(new DataWrap(new TariffsWrap(creditService.getTariffs()))));
    }

    @PostMapping("order")
    @TimeLimiter(name = "oneSecTimeLimiter")
    public CompletableFuture<ResponseEntity<DataWrap>> createOrder(@RequestBody LoanOrderCreateDto dto) {
        return CompletableFuture.supplyAsync(() ->  ResponseEntity.ok(new DataWrap(new OrderIdWrap(creditService.createOrder(dto).getOrderId()))));
    }

    @GetMapping("getStatusOrder")
    @TimeLimiter(name = "oneSecTimeLimiter")
    public CompletableFuture<ResponseEntity<DataWrap>> getStatusOrder(@RequestParam("orderId") UUID orderId) {
        return CompletableFuture.supplyAsync(() -> ResponseEntity.ok(new DataWrap(new OrderStatusWrap(creditService.getStatusOrder(orderId).getStatus()))));
    }

    @DeleteMapping("deleteOrder")
    @TimeLimiter(name = "oneSecTimeLimiter")
    public CompletableFuture<ResponseEntity<Void>> deleteOrder(@RequestBody LoanOrderDeleteDto dto) {
        return CompletableFuture.supplyAsync(() -> {
            creditService.deleteOrder(dto);
            return ResponseEntity.ok().build();
        });
    }
}
