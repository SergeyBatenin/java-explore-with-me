package ru.practicum.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import ru.practicum.ewm.StatClient;

@Configuration
public class StatConfig {

    @Bean
    public StatClient statClient(@Value("${client.url}") String baseUrl) {
        WebClient webClient = WebClient.create(baseUrl);
        return new StatClient(webClient);
    }
}
