package ru.practicum.ewm.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.ParamHitDto;
import ru.practicum.ewm.dto.ParamStatDto;
import ru.practicum.ewm.dto.StatInfoDto;
import ru.practicum.ewm.mapper.ParamMapper;
import ru.practicum.ewm.model.ParamHit;
import ru.practicum.ewm.repository.StatRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StatServiceImpl implements StatService {
    private final StatRepository repository;
    private final ParamMapper paramMapper;

    @Override
    public void add(ParamHitDto paramHitDto) {
        ParamHit paramHit = paramMapper.fromDto(paramHitDto);
        repository.save(paramHit);
    }

    @Override
    public List<StatInfoDto> getWithoutUris(ParamStatDto paramDto) {
        if (paramDto.getUnique()) {
            return repository.getAllWithUniqueIpAndWithoutUris(paramDto.getStart(), paramDto.getEnd());
        } else {
            return repository.getAllNotUniqueIpAndWithoutUris(paramDto.getStart(), paramDto.getEnd());
        }
    }

    @Override
    public List<StatInfoDto> getWithUris(ParamStatDto paramDto) {
        if (paramDto.getUnique()) {
            return repository.getAllWithUniqueIpAndWithUris(paramDto.getStart(), paramDto.getEnd(), paramDto.getUris());
        } else {
            return repository.getAllNotUniqueIpAndWithUris(paramDto.getStart(), paramDto.getEnd(), paramDto.getUris());
        }
    }
}
