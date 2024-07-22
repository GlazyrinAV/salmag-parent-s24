package ru.avg.customerapp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import ru.avg.customerapp.client.WebClientProductsClient;

@Configuration
public class ClientConfig {

    @Bean
    public WebClientProductsClient webClientProductsClient(
            @Value("${selmag.service.catalogue.uri:http://localhost:8081}") String uri) {
        return new WebClientProductsClient(WebClient.builder()
                .baseUrl(uri)
                .build());
    }
}
