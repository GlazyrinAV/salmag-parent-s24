package ru.avg.customerapp.client;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ProblemDetail;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.avg.customerapp.client.dto.NewProductReview;
import ru.avg.customerapp.entity.ProductReview;
import ru.avg.customerapp.exception.ClientBadRequestException;

import java.util.List;

@RequiredArgsConstructor
public class ClientProductsReviewClient implements ProductsReviewClient {

    private final WebClient webClient;

    @Override
    public Flux<ProductReview> findProductReviewsByProductId(Integer productId) {
        return this.webClient.get()
                .uri("/feedback-api/product-reviews/by-product-id/{productId}", productId)
                .retrieve()
                .bodyToFlux(ProductReview.class);
    }

    @Override
    public Mono<ProductReview> createProductReview(Integer productId, Integer rating, String review) {
        return this.webClient.post()
                .uri("/feedback-api/product-reviews")
                .bodyValue(new NewProductReview(productId, rating, review))
                .retrieve()
                .bodyToMono(ProductReview.class)
                .onErrorMap(WebClientResponseException.BadRequest.class, ex ->
                        new ClientBadRequestException(ex,
                                ((List<String>) ex.getResponseBodyAs(ProblemDetail.class).getProperties().get("errors"))));
    }


}
