package ru.avg.customerapp.client;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ProblemDetail;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.avg.customerapp.client.dto.NewFavoriteProduct;
import ru.avg.customerapp.entity.FavoriteProduct;
import ru.avg.customerapp.exception.ClientBadRequestException;

import java.util.List;

@RequiredArgsConstructor
public class ClientFavoriteProductsClient implements FavoriteProductsClient {

    private final WebClient webClient;

    @Override
    public Flux<FavoriteProduct> findAllFavoriteProducts() {
        return webClient.get()
                .uri("/feedback-api/favorite-products")
                .retrieve()
                .bodyToFlux(FavoriteProduct.class);
    }

    @Override
    public Mono<FavoriteProduct> findFavoriteProduct(Integer productId) {
        return webClient.get()
                .uri("/feedback-api/favorite-products/by-product-id/{productId}", productId)
                .retrieve()
                .bodyToMono(FavoriteProduct.class)
                .onErrorComplete(WebClientResponseException.NotFound.class);
    }

    @Override
    public Mono<FavoriteProduct> addProductToFavorites(Integer productId) {
        return this.webClient.post()
                .uri("/feedback-api/favorite-products")
                .bodyValue(new NewFavoriteProduct(productId))
                .retrieve()
                .bodyToMono(FavoriteProduct.class)
                .onErrorMap(WebClientResponseException.BadRequest.class, ex ->
                        new ClientBadRequestException(ex,
                                ((List<String>) ex.getResponseBodyAs(ProblemDetail.class).getProperties().get("errors"))));
    }

    @Override
    public Mono<Void> removeProductFromFavorites(Integer productId) {
        return this.webClient.delete()
                .uri("/feedback-api/favorite-products/by-product-id/{productId}", productId)
                .retrieve()
                .toBodilessEntity()
                .then();
    }
}
