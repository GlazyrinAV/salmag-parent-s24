package ru.avg.customerapp.controller;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockUser;

@SpringBootTest
@AutoConfigureWebTestClient
@WireMockTest(httpPort = 54321)
class ProductControllerIT {

    @Autowired
    private WebTestClient webClient;

    @Test
    void addToFavorites_RequestIsValid_ReturnsRediractionToProductPage() {
        //given
        WireMock.stubFor(WireMock.get("/catalogue-api/products/1")
                .willReturn(WireMock.okJson("""
                                {"id" :  1, "title":  "title 1", "details" : "details 1"}
                                """)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)));
        WireMock.stubFor(WireMock.post("/feedback-api/favorite-products")
                .withRequestBody(WireMock.equalToJson("""
                        {"productId" :  1}
                        """))
                .withHeader(HttpHeaders.CONTENT_TYPE, equalTo(MediaType.APPLICATION_JSON_VALUE)
                )
                .willReturn(WireMock.created()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody("""
                                {"id": "35a3ddb4-4a8b-11ef-8e49-00155deaee3f","productId" : 1}
                                """)));

        //when
        webClient
                .mutateWith(mockUser())
                .mutateWith(csrf())
                .post()
                .uri("/customer/products/1/add-to-favorites")
                .exchange()
                //then
                .expectStatus().is3xxRedirection()
                .expectHeader().location("/customer/products/1");

        WireMock.verify(getRequestedFor(urlPathMatching("/catalogue-api/products/1")));
        WireMock.verify(postRequestedFor(urlPathMatching("/feedback-api/favorite-products"))
                .withRequestBody(equalToJson("""
                        {"productId" :  1}
                        """)));
    }

    @Test
    void addToFavorites_NoProduct_ReturnNotFoundPage() {
        //given

        //when
        webClient
                .mutateWith(mockUser())
                .mutateWith(csrf())
                .post()
                .uri("/customer/products/1/add-to-favorites")
                .exchange()
                //then
                .expectStatus().isNotFound();

        WireMock.verify(getRequestedFor(urlPathMatching("/catalogue-api/products/1")));
    }
}