package com.example.mtsfinalproject.mapper;

import com.example.mtsfinalproject.dto.TariffDto;
import com.example.mtsfinalproject.entity.TariffEntity;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface TariffMapper {

    TariffDto toDto(TariffEntity entity);

}
