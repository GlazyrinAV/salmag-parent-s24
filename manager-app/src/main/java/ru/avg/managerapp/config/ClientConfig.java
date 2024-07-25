package ru.avg.managerapp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.web.client.RestClient;
import ru.avg.managerapp.client.RestClientProductRestClient;
import ru.avg.managerapp.security.OAuthClientRequestInterceptor;

@Configuration
public class ClientConfig {

    @Bean
    public RestClientProductRestClient restClientProductRestClient(
            @Value("${selmag.services.catalogue.uri:http://localhost:8081}") String catalogueBaseUri,
            ClientRegistrationRepository clientRegistrationRepository,
            OAuth2AuthorizedClientRepository authorizedClientRepository,
            @Value("${selmag.services.catalogue.registration-id:keycloak}") String registrationId) {
        return new RestClientProductRestClient(RestClient.builder()
                .baseUrl(catalogueBaseUri)
                .requestInterceptor(new OAuthClientRequestInterceptor(
                        new DefaultOAuth2AuthorizedClientManager(clientRegistrationRepository,
                                authorizedClientRepository), registrationId))
                .build());
    }
}