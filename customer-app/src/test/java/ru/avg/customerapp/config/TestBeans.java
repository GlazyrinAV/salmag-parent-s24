package ru.avg.customerapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository;
import org.springframework.web.reactive.function.client.WebClient;
import ru.avg.customerapp.client.ClientFavoriteProductsClient;
import ru.avg.customerapp.client.ClientProductsReviewClient;
import ru.avg.customerapp.client.WebClientProductsClient;

import static org.mockito.Mockito.mock;

@Configuration
public class TestBeans {

    @Bean
    public ReactiveClientRegistrationRepository clientRegistrationRepository() {
        return mock();
    }

    @Bean
    public ServerOAuth2AuthorizedClientRepository authorizedClientRepository() {
        return mock();
    }

    @Bean
    @Primary
    public WebClientProductsClient mockWebClientProductsClientT() {
        return new WebClientProductsClient(WebClient.builder()
                .baseUrl("http://localhost:54321")
                .build());
    }

    @Bean
    @Primary
    public ClientFavoriteProductsClient mockClientFavoriteProductsClient() {
        return new ClientFavoriteProductsClient(WebClient.builder()
                .baseUrl("http://localhost:54321")
                .build());
    }

    @Bean
    @Primary
    public ClientProductsReviewClient mockClientProductsReviewClient() {
        return new ClientProductsReviewClient(WebClient.builder()
                .baseUrl("http://localhost:54321")
                .build());
    }
}
