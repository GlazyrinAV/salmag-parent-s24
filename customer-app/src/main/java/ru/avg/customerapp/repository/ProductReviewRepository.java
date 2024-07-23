package ru.avg.customerapp.repository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.avg.customerapp.entity.ProductReview;

public interface ProductReviewRepository {

    Mono<ProductReview> saveReview(ProductReview productReview);

    Flux<ProductReview> findAllByProductId(int productId);
}
