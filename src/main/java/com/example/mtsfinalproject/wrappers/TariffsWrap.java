package com.example.mtsfinalproject.wrappers;

import com.example.mtsfinalproject.dto.TariffDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class TariffsWrap {
    private List<TariffDto> tariffs;
}
