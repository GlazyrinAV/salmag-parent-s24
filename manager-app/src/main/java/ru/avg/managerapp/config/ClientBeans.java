package ru.avg.managerapp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import ru.avg.managerapp.client.RestClientProductRestClient;

@Configuration
public class ClientBeans {

    @Bean
    public RestClientProductRestClient restClientProductRestClient(
            @Value("${selmag.services.catalogue.uri:http://localhost:8081}") String catalogueBaseUri) {
        return new RestClientProductRestClient(RestClient.builder()
                .baseUrl(catalogueBaseUri)
                .build());
    }
}
