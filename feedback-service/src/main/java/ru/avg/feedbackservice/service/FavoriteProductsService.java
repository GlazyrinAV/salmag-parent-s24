package ru.avg.feedbackservice.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.avg.feedbackservice.entity.FavoriteProduct;

public interface FavoriteProductsService {

    Mono<FavoriteProduct> addProductToFavorites(int productId);

    Mono<Void> removeProductFromFavorites(int productId);

    Mono<FavoriteProduct> findFavoriteProduct(int productId);

    Flux<FavoriteProduct> findAllFavoriteProducts();
}