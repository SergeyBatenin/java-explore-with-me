package ru.practicum.ewm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParamStatDto implements Validator {
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime start;
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime end;
    private List<String> uris;
    private Boolean unique = false;


    @Override
    public boolean supports(Class<?> clazz) {
        return ParamStatDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ParamStatDto dto = (ParamStatDto) target;
        if (!dto.getEnd().isAfter(dto.getStart())) {
            errors.rejectValue("end", "date.range.invalid");
        }
    }
}
