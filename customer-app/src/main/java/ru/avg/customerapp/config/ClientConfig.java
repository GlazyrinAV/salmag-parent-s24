package ru.avg.customerapp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository;
import org.springframework.web.reactive.function.client.WebClient;
import ru.avg.customerapp.client.ClientFavoriteProductsClient;
import ru.avg.customerapp.client.ClientProductsReviewClient;
import ru.avg.customerapp.client.WebClientProductsClient;

@Configuration
public class ClientConfig {

    @Bean()
    @Scope("prototype")
    public WebClient.Builder selmagWebClientBuilder(ReactiveClientRegistrationRepository clientRegistrationRepository,
                                                    ServerOAuth2AuthorizedClientRepository authorizedClientRepository) {
        ServerOAuth2AuthorizedClientExchangeFilterFunction filter = new ServerOAuth2AuthorizedClientExchangeFilterFunction
                (clientRegistrationRepository, authorizedClientRepository);
        filter.setDefaultClientRegistrationId("keycloak");
        return WebClient.builder()
                .filter(filter);
    }

    @Bean
    public WebClientProductsClient webClientProductsClient(
            @Value("${selmag.service.catalogue.uri:http://localhost:8081}") String uri,
            WebClient.Builder selmagWebClientBuilder) {
        return new WebClientProductsClient(selmagWebClientBuilder
                .baseUrl(uri)
                .build());
    }

    @Bean
    public ClientFavoriteProductsClient clientFavoriteProductsClient(
            @Value("${selmag.service.feedback.uri:http://localhost:8084}") String uri,
            WebClient.Builder selmagWebClientBuilder) {
        return new ClientFavoriteProductsClient(selmagWebClientBuilder
                .baseUrl(uri)
                .build());
    }

    @Bean
    public ClientProductsReviewClient clientProductsReviewClient(
            @Value("${selmag.service.feedback.uri:http://localhost:8084}") String uri,
            WebClient.Builder selmagWebClientBuilder) {
        return new ClientProductsReviewClient(selmagWebClientBuilder
                .baseUrl(uri)
                .build());
    }
}
