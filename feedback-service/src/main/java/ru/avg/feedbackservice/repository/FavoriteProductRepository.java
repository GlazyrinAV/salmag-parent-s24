package ru.avg.feedbackservice.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.avg.feedbackservice.entity.FavoriteProduct;

import java.util.UUID;

public interface FavoriteProductRepository extends ReactiveCrudRepository<FavoriteProduct, UUID> {

    Mono<Void> deleteByProductIdAndUserId(int productId, String userId);

    Flux<FavoriteProduct> findAllByUserId(String userId);

    Mono<FavoriteProduct> findByProductIdAndUserId(int productId, String userId);
}
