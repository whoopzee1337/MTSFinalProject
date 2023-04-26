package com.example.mtsfinalproject.job;

import com.example.mtsfinalproject.entity.LoanOrderEntity;
import com.example.mtsfinalproject.repository.LoanOrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor
@Slf4j
public class LoanOrderJob {

    private final LoanOrderRepository loanOrderRepository;

    @Scheduled(fixedDelay = 120000)
    public void processLoanOrders() {
        Random random = new Random();
        List<LoanOrderEntity> inProgressOrders = loanOrderRepository.findAll("IN_PROGRESS");
        for (LoanOrderEntity order : inProgressOrders) {
            boolean approved = random.nextBoolean();
            String status = approved ? "APPROVED" : "REFUSED";
            loanOrderRepository.updateStatus(status, order.getUserId(), order.getOrderId());
            log.info("{} {} {}", order.getUserId(), order.getOrderId(), status);
        }
    }
}
