package com.example.mtsfinalproject.repository;

import com.example.mtsfinalproject.entity.TariffEntity;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class TariffRepository {

    private static final String SQL_SELECT_ALL_TARIFFS = "SELECT * FROM TARIFF";
    private static final String SQL_CHECK_EXISTING_BY_TARIFF_ID = "SELECT COUNT(*) FROM TARIFF WHERE ID = ?";

    private final JdbcTemplate jdbcTemplate;

    public List<TariffEntity> findAll() {
        return jdbcTemplate.query(SQL_SELECT_ALL_TARIFFS, new BeanPropertyRowMapper<>(TariffEntity.class));
    }

    public boolean isExist(Long tariffId) {
        int result = jdbcTemplate.queryForObject(SQL_CHECK_EXISTING_BY_TARIFF_ID, Integer.class, tariffId);
        return result > 0;
    }


}
