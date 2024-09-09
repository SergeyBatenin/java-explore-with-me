package ru.practicum.ewm.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.ewm.dto.ParamHitDto;
import ru.practicum.ewm.model.ParamHit;

@Mapper(componentModel = "spring")
public interface ParamMapper {
    @Mapping(target = "id", ignore = true)
    ParamHit fromDto(ParamHitDto paramHitDto);
}
