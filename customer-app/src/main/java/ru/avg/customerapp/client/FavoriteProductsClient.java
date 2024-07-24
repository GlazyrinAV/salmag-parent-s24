package ru.avg.customerapp.client;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.avg.customerapp.entity.FavoriteProduct;

public interface FavoriteProductsClient {

    Flux<FavoriteProduct> findAllFavoriteProducts();

    Mono<FavoriteProduct> findFavoriteProduct(Integer productId);

    Mono<FavoriteProduct> addProductToFavorites(Integer productId);

    Mono<Void> removeProductFromFavorites(Integer productId);
}
