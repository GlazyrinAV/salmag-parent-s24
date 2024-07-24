package ru.avg.feedbackservice.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.avg.feedbackservice.entity.ProductReview;

public interface ProductReviewsService {

    Mono<ProductReview> createReview(int productId, int rating, String review);

    Flux<ProductReview> findProductReviewsForProduct(int productId);
}
