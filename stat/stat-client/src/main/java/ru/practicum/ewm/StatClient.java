package ru.practicum.ewm;

import org.springframework.web.reactive.function.client.WebClient;
import ru.practicum.ewm.dto.ParamHitDto;
import ru.practicum.ewm.dto.ParamStatDto;
import ru.practicum.ewm.dto.StatInfoDto;

import java.time.LocalDateTime;
import java.util.List;

public class StatClient {
    private final WebClient client;

    public StatClient(WebClient client) {
        this.client = client;
    }

    public void save(String app, String uri, String ip, LocalDateTime timestamp) {
        ParamHitDto payload = new ParamHitDto(app, uri, ip, timestamp);
        client.post()
                .uri("/hit")
                .bodyValue(payload)
                .retrieve()
                .toBodilessEntity()
                .then();
    }

    public List<StatInfoDto> getStats(ParamStatDto paramDto) {
        return client.get()
                .uri("/stats")
                .retrieve()
                .bodyToFlux(StatInfoDto.class)
                .collectList()
                .block();
    }
}
