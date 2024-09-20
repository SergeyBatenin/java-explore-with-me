package ru.practicum.ewm;

import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import ru.practicum.ewm.dto.ParamHitDto;
import ru.practicum.ewm.dto.ParamStatDto;
import ru.practicum.ewm.dto.StatInfoDto;

import java.util.List;
import java.util.Optional;

public class StatClient {
    private final WebClient client;

    public StatClient(WebClient client) {
        this.client = client;
    }

    public Mono<Void> save(ParamHitDto payload) {
        return client.post()
                .uri("/hit")
                .bodyValue(payload)
                .retrieve()
                .toBodilessEntity()
                .doOnNext(response -> System.out.println("Данные статистики успешно сохранены"))
                .doOnError(e -> {
                    if (e instanceof WebClientResponseException wcre) {
                        System.err.println("Ошибка HTTP: " + wcre.getStatusCode());
                        System.err.println("Сообщение ошибки: " + wcre.getResponseBodyAsString());
                    } else {
                        System.err.println("Необработанная ошибка: " + e.getMessage());
                    }
                })
                .onErrorResume(e -> {
                    if (e instanceof WebClientResponseException) {
                        return Mono.empty();
                    }
                    return Mono.error(e);
                })
                .then();
    }

    public List<StatInfoDto> getStats(ParamStatDto paramDto) {
        return client.get()
                .uri(builder -> builder.path("/stats")
                        .queryParam("start", paramDto.getStart())
                        .queryParam("end", paramDto.getEnd())
                        .queryParamIfPresent("unique", Optional.ofNullable(paramDto.getUnique()))
                        .queryParamIfPresent("uris", Optional.ofNullable(paramDto.getUris()))
                        .build())
                .retrieve()
                .bodyToFlux(StatInfoDto.class)
                .collectList()
                .block();
    }
}
