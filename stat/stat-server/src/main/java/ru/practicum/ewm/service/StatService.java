package ru.practicum.ewm.service;

import ru.practicum.ewm.dto.ParamHitDto;
import ru.practicum.ewm.dto.ParamStatDto;
import ru.practicum.ewm.dto.StatInfoDto;

import java.util.List;

public interface StatService {
    void add(ParamHitDto paramHitDto);

    List<StatInfoDto> getWithoutUris(ParamStatDto paramDto);

    List<StatInfoDto> getWithUris(ParamStatDto paramDto);
}
