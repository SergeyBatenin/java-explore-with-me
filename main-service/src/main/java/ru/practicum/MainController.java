package ru.practicum;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.StatClient;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MainController {
    private final StatClient statClient;
}
