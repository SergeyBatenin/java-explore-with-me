package ewm;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParamHitDto {
    @NotBlank
    private String app;
    @NotBlank
    private String uri;
    @Pattern(regexp = "^((25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)(\\.|$)){4}$",
            message = "Некорректный формат IP-адреса")
    private String ip;
    @NotNull
    private LocalDateTime timestamp;
}
