package ru.avg.customerapp.repository;

import reactor.core.publisher.Mono;
import ru.avg.customerapp.entity.FavoriteProduct;

public interface FavoriteProductRepository {

    Mono<FavoriteProduct> save(FavoriteProduct favoriteProduct);

    Mono<Void> deleteByProductId(int productId);
}