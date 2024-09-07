package ewm.service;

import ewm.ParamDto;
import ewm.ParamHitDto;
import ewm.StatDto;
import ewm.mapper.ParamMapper;
import ewm.model.ParamHit;
import ewm.repository.StatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
    public List<StatDto> getWithoutUris(ParamDto paramDto) {
        if (paramDto.getUnique()) {
            return repository.getAllWithUniqueIpAndWithoutUris(paramDto.getStart(), paramDto.getEnd());
        } else {
            return repository.getAllNotUniqueIpAndWithoutUris(paramDto.getStart(), paramDto.getEnd());
        }
    }

    @Override
    public List<StatDto> getWithUris(ParamDto paramDto) {
        if (paramDto.getUnique()) {
            return repository.getAllWithUniqueIpAndWithUris(paramDto.getStart(), paramDto.getEnd(), paramDto.getUris());
        } else {
            return repository.getAllNotUniqueIpAndWithUris(paramDto.getStart(), paramDto.getEnd(), paramDto.getUris());
        }
    }
}
