package ru.practicum.ewm;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;
import ru.practicum.ewm.dto.ParamHitDto;
import ru.practicum.ewm.dto.ParamStatDto;
import ru.practicum.ewm.dto.StatInfoDto;

import java.util.List;
import java.util.Optional;

public class StatClient {
    private final RestClient restClient;

    public StatClient(RestClient restClient) {
        this.restClient = restClient;
    }

    public void save(ParamHitDto payload) {
        restClient.post()
                .uri("/hit")
                .contentType(MediaType.APPLICATION_JSON)
                .body(payload) // Здесь укажите данные, которые хотите отправить
                .retrieve()
                .onStatus(HttpStatusCode::is2xxSuccessful,
                        ((request, response) -> System.out.println("Данные статистики успешно сохранены")))
                .onStatus(HttpStatusCode::isError,
                        (request, response) -> System.err.printf("ERROR:\n\tStatusCode=%d, Headers=%s",
                                response.getStatusCode().value(), response.getHeaders()))
                .toBodilessEntity();
    }

    public List<StatInfoDto> getStats(ParamStatDto paramDto) {

        return restClient.get()
                .uri(uriBuilder -> uriBuilder.path("/stats")
                        .queryParam("start", paramDto.getStart().withNano(0))
                        .queryParam("end", paramDto.getEnd().withNano(0))
                        .queryParamIfPresent("unique", Optional.ofNullable(paramDto.getUnique()))
                        .queryParamIfPresent("uris", Optional.ofNullable(paramDto.getUris()))
                        .build())
                .retrieve()
                .body(new ParameterizedTypeReference<List<StatInfoDto>>() {
                });
    }
}
