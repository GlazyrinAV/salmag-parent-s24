package ru.avg.feedbackservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.avg.feedbackservice.entity.ProductReview;
import ru.avg.feedbackservice.repository.ProductReviewRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DefaultProductReviewsService implements ProductReviewsService {

    private final ProductReviewRepository productReviewRepository;

    @Override
    public Mono<ProductReview> createReview(int productId, int rating, String review) {
        return this.productReviewRepository.saveReview(
                new ProductReview(UUID.randomUUID(), productId, rating, review)
        );
    }

    @Override
    public Flux<ProductReview> findProductReviewsForProduct(int productId) {
        return this.productReviewRepository.findAllByProductId(productId);
    }
}
