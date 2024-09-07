package ewm;

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
public class ParamDto implements Validator {
    @NotNull
    private LocalDateTime start;
    @NotNull
    private LocalDateTime end;
    private List<String> uris;
    private Boolean unique = false;

    @Override
    public boolean supports(Class<?> clazz) {
        return ParamDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ParamDto dto = (ParamDto) target;
        if (!dto.getEnd().isAfter(dto.getStart())) {
            errors.rejectValue("end", "date.range.invalid");
        }
    }
}
