package ewm.service;

import ewm.ParamDto;
import ewm.ParamHitDto;
import ewm.StatDto;

import java.util.List;

public interface StatService {
    void add(ParamHitDto paramHitDto);

    List<StatDto> getWithoutUris(ParamDto paramDto);

    List<StatDto> getWithUris(ParamDto paramDto);
}
