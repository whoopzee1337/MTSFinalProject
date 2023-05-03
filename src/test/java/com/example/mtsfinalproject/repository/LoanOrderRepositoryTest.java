package com.example.mtsfinalproject.repository;

import com.example.mtsfinalproject.AbstractTestcontainers;
import com.example.mtsfinalproject.entity.LoanOrderEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class LoanOrderRepositoryTest extends AbstractTestcontainers {
    private LoanOrderRepository loanOrderRepository;

    @BeforeEach
    void setUp() {
        loanOrderRepository = new LoanOrderRepository(getJdbcTemplate());

    }

    @Test
    void findAll() {
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());

        LoanOrderEntity loanOrderEntity = new LoanOrderEntity();
        loanOrderEntity.setOrderId(UUID.randomUUID())
                .setUserId(1L)
                .setTariffId(1L)
                .setCreditRating(0.5)
                .setStatus("IN_PROGRESS")
                .setTimeInsert(currentTimestamp)
                .setTimeUpdate(currentTimestamp);

        loanOrderRepository.save(loanOrderEntity);

        List<LoanOrderEntity> loanOrderEntities1 = loanOrderRepository.findAll(loanOrderEntity.getUserId());
        List<LoanOrderEntity> loanOrderEntities2 = loanOrderRepository.findAll(loanOrderEntity.getStatus());

        assertThat(loanOrderEntities1).isNotEmpty();
        assertThat(loanOrderEntities2).isNotEmpty();
    }

    @Test
    void save() {
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());

        LoanOrderEntity loanOrderEntity = new LoanOrderEntity();
        loanOrderEntity.setOrderId(UUID.randomUUID())
                .setUserId(1L)
                .setTariffId(1L)
                .setCreditRating(0.5)
                .setStatus("IN_PROGRESS")
                .setTimeInsert(currentTimestamp)
                .setTimeUpdate(currentTimestamp);

        assertThat(loanOrderRepository.isExist(loanOrderEntity.getOrderId())).isFalse();
        loanOrderRepository.save(loanOrderEntity);
        assertThat(loanOrderRepository.isExist(loanOrderEntity.getOrderId())).isTrue();

    }

    @Test
    void isExist() {
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());

        UUID orderId = UUID.randomUUID();
        assertThat(loanOrderRepository.isExist(orderId)).isFalse();

        Long userId = 1L;
        assertThat(loanOrderRepository.isExist(userId, orderId)).isFalse();

        LoanOrderEntity loanOrderEntity = new LoanOrderEntity()
                .setOrderId(orderId)
                .setUserId(userId)
                .setTariffId(1L)
                .setCreditRating(0.5)
                .setStatus("IN_PROGRESS")
                .setTimeInsert(currentTimestamp)
                .setTimeUpdate(currentTimestamp);

        loanOrderRepository.save(loanOrderEntity);

        assertThat(loanOrderRepository.isExist(orderId)).isTrue();
        assertThat(loanOrderRepository.isExist(userId, orderId)).isTrue();
    }


    @Test
    void getOrder() {
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());

        UUID orderId = UUID.randomUUID();

        LoanOrderEntity loanOrderEntity = new LoanOrderEntity()
                .setOrderId(orderId)
                .setUserId(1L)
                .setTariffId(1L)
                .setCreditRating(0.5)
                .setStatus("IN_PROGRESS")
                .setTimeInsert(currentTimestamp)
                .setTimeUpdate(currentTimestamp);

        loanOrderRepository.save(loanOrderEntity);

        LoanOrderEntity actual = loanOrderRepository.getOrder(orderId);

        assertThat(actual).isNotNull();
        assertThat(actual.getOrderId()).isEqualTo(orderId);
    }

    @Test
    void deleteOrder() {
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());

        UUID orderId = UUID.randomUUID();
        Long userId = 1L;

        LoanOrderEntity loanOrderEntity = new LoanOrderEntity()
                .setOrderId(orderId)
                .setUserId(userId)
                .setTariffId(1L)
                .setCreditRating(0.5)
                .setStatus("IN_PROGRESS")
                .setTimeInsert(currentTimestamp)
                .setTimeUpdate(currentTimestamp);

        loanOrderRepository.save(loanOrderEntity);
        assertThat(loanOrderRepository.isExist(orderId)).isTrue();
        loanOrderRepository.deleteOrder(userId, orderId);
        assertThat(loanOrderRepository.isExist(orderId)).isFalse();
    }

    @Test
    void updateStatus() {
        UUID orderId = UUID.randomUUID();
        Long userId = 1L;

        LoanOrderEntity loanOrderEntity = new LoanOrderEntity()
                .setOrderId(orderId)
                .setUserId(1L)
                .setTariffId(1L)
                .setCreditRating(0.5)
                .setStatus("IN_PROGRESS")
                .setTimeInsert(new Timestamp(System.currentTimeMillis()))
                .setTimeUpdate(new Timestamp(System.currentTimeMillis()));

        loanOrderRepository.save(loanOrderEntity);
        assertThat(loanOrderRepository.getOrder(orderId).getStatus()).isEqualTo("IN_PROGRESS");
        loanOrderRepository.updateStatus("APPROVED", userId, orderId);
        assertThat(loanOrderRepository.getOrder(orderId).getStatus()).isEqualTo("APPROVED");
    }
}