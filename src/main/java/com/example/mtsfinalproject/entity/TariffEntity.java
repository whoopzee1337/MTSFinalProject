package com.example.mtsfinalproject.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Entity
@Table(name = "tariff")
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class TariffEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type", nullable = false, unique = true)
    private String type;

    @Column(name = "interest_rate", nullable = false)
    private String interestRate;
}
