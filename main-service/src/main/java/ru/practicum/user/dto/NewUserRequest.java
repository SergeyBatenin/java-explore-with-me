package ru.practicum.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewUserRequest {
    @NotBlank
    @Size(min = 2, message = "Имя не может быть меньше 2 символов")
    @Size(max = 250, message = "Имя не может быть больше 250 символов")
    private String name;

    @NotNull
    @Size(min = 6, message = "Адрес электронной почты не может быть меньше 6 символов")
    @Size(max = 254, message = "Адрес электронной почты не может быть больше 254 символов")
    @Email
    private String email;
}
