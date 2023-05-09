package com.example.mtsfinalproject.wrappers;

import com.example.mtsfinalproject.dto.LoanOrderDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class LoanOrdersWrap {
    private List<LoanOrderDto> orders;
}
