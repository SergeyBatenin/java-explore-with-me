package ru.practicum.ewm.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.practicum.ewm.dto.ParamStatDto;

@Component
public class ParamStatDtoValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return ParamStatDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ParamStatDto dto = (ParamStatDto) target;
        if (dto.getStart().isAfter(dto.getEnd())) {
            errors.rejectValue("end", "date.range.invalid");
        }
    }
}
