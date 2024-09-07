package ewm.client;

import ewm.ParamDto;
import ewm.ParamHitDto;
import ewm.StatDto;
import org.springframework.web.reactive.function.client.WebClient;

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

    public List<StatDto> getStats(ParamDto paramDto) {
        return client.get()
                .uri("/stats")
                .retrieve()
                .bodyToFlux(StatDto.class)
                .collectList()
                .block();
    }
}
