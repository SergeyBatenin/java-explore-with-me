package ru.practicum.ewm;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.dto.ParamHitDto;
import ru.practicum.ewm.dto.ParamStatDto;
import ru.practicum.ewm.dto.StatInfoDto;
import ru.practicum.ewm.service.StatService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@Slf4j
public class StatController {
    private final StatService statService;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void add(@RequestBody @Valid ParamHitDto paramHitDto) {
        log.info("POST /hit request: {}", paramHitDto);
        statService.add(paramHitDto);
        log.info("POST /hit response: success");
    }

    @GetMapping("/stats")
    public List<StatInfoDto> get(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                 @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                 @RequestParam(required = false) List<String> uris,
                                 @RequestParam(defaultValue = "false") boolean unique) {
        ParamStatDto paramStatDto = new ParamStatDto(start, end, uris, unique);
        log.info("GEt /stats request: {}", paramStatDto);

        List<StatInfoDto> statDtos;
        if (paramStatDto.getUris() == null) {
            statDtos = statService.getWithoutUris(paramStatDto);
        } else {
            statDtos = statService.getWithUris(paramStatDto);
        }

        log.info("GET /stats response: {} element(s)", statDtos.size());
        return statDtos;
    }
}
