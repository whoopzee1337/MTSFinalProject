package com.example.mtsfinalproject.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class TariffDto {

    private Long id;

    private String type;

    private String interestRate;
}
