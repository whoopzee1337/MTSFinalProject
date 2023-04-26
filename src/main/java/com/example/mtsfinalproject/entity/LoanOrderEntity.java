package com.example.mtsfinalproject.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class LoanOrderEntity {

    @Id
    private Long id;

    private UUID orderId;

    private Long userId;

    private Long tariffId;

    private Double creditRating;

    private String status;

    private Timestamp timeInsert;

    private Timestamp timeUpdate;
}
