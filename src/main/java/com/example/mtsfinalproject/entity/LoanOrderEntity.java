package com.example.mtsfinalproject.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "loan_order")
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class LoanOrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id", nullable = false, unique = true)
    private UUID orderId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "tariff_id", nullable = false)
    private Long tariffId;

    @Column(name = "credit_rating", nullable = false)
    private Double creditRating;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "time_insert", nullable = false)
    private Timestamp timeInsert;

    @Column(name = "time_update", nullable = false)
    private Timestamp timeUpdate;
}
