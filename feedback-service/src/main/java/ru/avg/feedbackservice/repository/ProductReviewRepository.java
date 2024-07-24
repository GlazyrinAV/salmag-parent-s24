package ru.avg.feedbackservice.repository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.avg.feedbackservice.entity.ProductReview;

public interface ProductReviewRepository {

    Mono<ProductReview> saveReview(ProductReview productReview);

    Flux<ProductReview> findAllByProductId(int productId);
}
