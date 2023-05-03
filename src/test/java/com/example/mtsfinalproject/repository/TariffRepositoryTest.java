package com.example.mtsfinalproject.repository;

import com.example.mtsfinalproject.AbstractTestcontainers;
import com.example.mtsfinalproject.entity.TariffEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class TariffRepositoryTest extends AbstractTestcontainers {

    private TariffRepository tariffRepository;

    @BeforeEach
    void setUp() {
        tariffRepository = new TariffRepository(getJdbcTemplate());
    }


    @Test
    void findAll() {
        TariffEntity tariff = new TariffEntity();
        tariff.setType(UUID.randomUUID().toString()).setInterestRate("test");

        tariffRepository.save(tariff);

        assertThat(tariffRepository.findAll()).isNotEmpty();
    }

    @Test
    void isExist() {
        assertThat(tariffRepository.isExist(-1L)).isFalse();
    }

    @Test
    void save() {
        TariffEntity tariff = new TariffEntity();
        tariff.setType(UUID.randomUUID().toString()).setInterestRate("test");

        tariffRepository.save(tariff);

        assertThat(tariffRepository.isExist(tariff.getId()));
    }

}