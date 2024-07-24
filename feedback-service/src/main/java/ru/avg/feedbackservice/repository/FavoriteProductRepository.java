package ru.avg.feedbackservice.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;
import ru.avg.feedbackservice.entity.FavoriteProduct;

import java.util.UUID;

public interface FavoriteProductRepository extends ReactiveCrudRepository<FavoriteProduct, UUID> {

    Mono<Void> deleteByProductId(int productId);

    Mono<FavoriteProduct> findByProductId(int productId);
}
