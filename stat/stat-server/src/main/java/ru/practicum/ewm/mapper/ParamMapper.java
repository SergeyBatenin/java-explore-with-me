package ru.practicum.ewm.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.ewm.dto.ParamHitDto;
import ru.practicum.ewm.model.ParamHit;

@Mapper(componentModel = "spring")
public interface ParamMapper {
//    @Mapping(target = "app", source = "paramHitDto.app")
//    @Mapping(target = "uri", source = "paramHitDto.uri")
//    @Mapping(target = "ip", source = "paramHitDto.ip")
//    @Mapping(target = "timestamp", source = "paramHitDto.timestamp")
    @Mapping(target = "id", ignore = true)
    ParamHit fromDto(ParamHitDto paramHitDto);
}
