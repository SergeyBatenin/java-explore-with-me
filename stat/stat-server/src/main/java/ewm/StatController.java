package ewm;

import ewm.service.StatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
public class StatController {
    private final StatService statService;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void add(@RequestBody ParamHitDto paramHitDto) {
        log.info("POST /hit request: {}", paramHitDto);
        statService.add(paramHitDto);
        log.info("POST /hit response: success");
    }

    @GetMapping("/stats")
    public List<StatDto> get(@ModelAttribute @Valid ParamDto paramDto) {
        log.info("GEt /stats request");
        List<StatDto> statDtos;
        if (paramDto.getUris() == null) {
            statDtos = statService.getWithoutUris(paramDto);
        } else {
            statDtos = statService.getWithUris(paramDto);
        }
        log.info("GET /stats response: {} element(s)", statDtos.size());
        return statDtos;
    }
}
