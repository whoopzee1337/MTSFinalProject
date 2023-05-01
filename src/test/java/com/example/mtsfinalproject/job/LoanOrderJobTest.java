package com.example.mtsfinalproject.job;

import com.example.mtsfinalproject.entity.LoanOrderEntity;
import com.example.mtsfinalproject.repository.LoanOrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoanOrderJobTest {

    @Mock
    private LoanOrderRepository loanOrderRepository;

    @InjectMocks
    private LoanOrderJob loanOrderJob;

    @Test
    void testProcessLoanOrders() {
        LoanOrderEntity order1 = new LoanOrderEntity();
        order1.setUserId(1L).setOrderId(UUID.randomUUID()).setStatus("IN_PROGRESS");

        LoanOrderEntity order2 = new LoanOrderEntity();
        order2.setUserId(2L).setOrderId(UUID.randomUUID()).setStatus("IN_PROGRESS");

        List<LoanOrderEntity> inProgressOrders = new ArrayList<>();
        inProgressOrders.add(order1);
        inProgressOrders.add(order2);

        when(loanOrderRepository.findAll("IN_PROGRESS")).thenReturn(inProgressOrders);

        loanOrderJob.processLoanOrders();

        verify(loanOrderRepository, times(2)).updateStatus(anyString(), anyLong(), any(UUID.class));
    }
}