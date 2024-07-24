package ru.avg.customerapp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import ru.avg.customerapp.client.ClientFavoriteProductsClient;
import ru.avg.customerapp.client.ClientProductsReviewClient;
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

    @Bean
    public ClientFavoriteProductsClient clientFavoriteProductsClient(
            @Value("${selmag.service.feedback.uri:http://localhost:8084}") String uri) {
        return new ClientFavoriteProductsClient(WebClient.builder()
                .baseUrl(uri)
                .build());
    }

    @Bean
    public ClientProductsReviewClient clientProductsReviewClient(
            @Value("${selmag.service.feedback.uri:http://localhost:8084}") String uri) {
        return new ClientProductsReviewClient(WebClient.builder()
                .baseUrl(uri)
                .build());
    }
}
