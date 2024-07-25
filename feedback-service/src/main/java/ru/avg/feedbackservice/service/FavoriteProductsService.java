package ru.avg.feedbackservice.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.avg.feedbackservice.entity.FavoriteProduct;

public interface FavoriteProductsService {

    Mono<FavoriteProduct> addProductToFavorites(int productId, String userId);

    Mono<Void> removeProductFromFavorites(int productId, String userId);

    Mono<FavoriteProduct> findFavoriteProduct(int productId, String userId);

    Flux<FavoriteProduct> findAllFavoriteProducts(String userId);
}
