package ewm.mapper;

import ewm.ParamHitDto;
import ewm.model.ParamHit;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ParamMapper {
    ParamHit fromDto(ParamHitDto paramHitDto);
}
