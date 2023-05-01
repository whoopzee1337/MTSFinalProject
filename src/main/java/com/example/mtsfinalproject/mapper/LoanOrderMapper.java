package com.example.mtsfinalproject.mapper;

import com.example.mtsfinalproject.dto.LoanOrderDto;
import com.example.mtsfinalproject.entity.LoanOrderEntity;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface LoanOrderMapper {

    LoanOrderDto toDto(LoanOrderEntity entity);

}
