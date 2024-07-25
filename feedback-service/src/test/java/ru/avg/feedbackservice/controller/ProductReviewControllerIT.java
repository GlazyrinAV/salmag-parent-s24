package ru.avg.feedbackservice.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import ru.avg.feedbackservice.entity.ProductReview;

import java.util.List;
import java.util.UUID;

import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockJwt;

@SpringBootTest
@AutoConfigureWebTestClient
class ProductReviewControllerIT {

    @Autowired
    WebTestClient webClient;

    @Autowired
    ReactiveMongoTemplate mongoTemplate;

    @BeforeEach
    void setUp() {
        this.mongoTemplate.insertAll(List.of(
                        new ProductReview(UUID.fromString("35a3ddb4-4a8b-11ef-8e49-00155deaee3f"),
                                1, 1, "review 1", "1"),
                        new ProductReview(UUID.fromString("3795aa8a-4a8b-11ef-a346-00155deaee3f"),
                                1, 2, "review 2", "2")
                ))
                .blockLast();
    }

    @AfterEach
    void tearDown() {
        this.mongoTemplate.remove(ProductReview.class).all().block();
    }

    @Test
    void findProductReviewsByProductId_returnsReviews() {
        //given

        //when
        this.webClient
                .mutateWith(mockJwt())
                .get()
                .uri("/feedback-api/product-reviews/by-product-id/1")
                .exchange()
                //then
                .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                .expectBody()
                .json("""
                        [
                        {"id" :  "35a3ddb4-4a8b-11ef-8e49-00155deaee3f", "productId" :  1, "rating" :  1, "review" :  "review 1", "userId" :  "1"},
                        {"id" :  "3795aa8a-4a8b-11ef-a346-00155deaee3f", "productId" :  1, "rating" :  2, "review" :  "review 2", "userId" :  "2"}
                        ]
                        """);
    }

    @Test
    void saveProductReview_RequestIsValid_ReturnsProductReview() {
        //given
        //when
        this.webClient
                .mutateWith(mockJwt().jwt(builder -> builder.subject("user-test")))
                .post()
                .uri("/feedback-api/product-reviews")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""
                        {"productId" :  1, "rating" :  5, "review" :  "review 3"}
                        """)
                .exchange()
                //then
                .expectStatus().isCreated()
                .expectHeader().exists(HttpHeaders.LOCATION)
                .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                .expectBody()
                .json("""
                        {"productId" :  1, "rating" :  5, "review" :  "review 3", "userId" :  "user-test"}
                        """)
                .jsonPath("$.id").exists();
    }

    @Test
    void saveProductReview_RequestIsInvalid_ReturnsBadRequest() {
        //given
        //when
        this.webClient
                .mutateWith(mockJwt().jwt(builder -> builder.subject("user-test")))
                .post()
                .uri("/feedback-api/product-reviews")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""
                        {"productId" :  null, "rating" :  6, "review" :  " "}
                        """)
                .exchange()
                //then
                .expectStatus().isBadRequest()
                .expectHeader().doesNotExist(HttpHeaders.LOCATION)
                .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON)
                .expectBody()
                .json("""
                        {"errors" : [
                        "Идентификатор товара должен быть указан",
                        "Рейтинг не должен быть больше {max}",
                        "Отзыв не должно быть пустым или состоять из пробелов"
                        ]}
                        """);
    }

    @Test
    void findProductReviewsByProductId_UserNotAuth_ReturnsNotAuthorized() {
        //given

        //when
        this.webClient
                .get()
                .uri("/feedback-api/product-reviews/by-product-id/1")
                .exchange()
                //then
                .expectStatus().isUnauthorized();
    }
}