package com.example.mtsfinalproject.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Entity
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class TariffEntity {

    @Id
    private Long id;

    private String type;

    private String interestRate;
}
