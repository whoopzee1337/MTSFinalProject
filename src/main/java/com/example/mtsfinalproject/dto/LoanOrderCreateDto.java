package com.example.mtsfinalproject.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class LoanOrderCreateDto {

    private Long userId;

    private Long tariffId;
}
