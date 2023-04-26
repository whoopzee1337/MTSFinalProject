package com.example.mtsfinalproject.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.sql.Timestamp;
import java.util.UUID;

@Data
@Accessors(chain = true)
public class LoanOrderDto {

    private Long id;

    private UUID orderId;

    private Long userId;

    private Long tariffId;

    private Double creditRating;

    private String status;

    private Timestamp timeInsert;

    private Timestamp timeUpdate;
}
