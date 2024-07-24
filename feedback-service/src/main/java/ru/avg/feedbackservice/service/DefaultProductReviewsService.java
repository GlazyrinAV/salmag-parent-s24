package ru.avg.feedbackservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
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

    private final ReactiveMongoTemplate reactiveMongoTemplate;

    @Override
    public Mono<ProductReview> createReview(int productId, int rating, String review) {
        return this.productReviewRepository.save(
                new ProductReview(UUID.randomUUID(), productId, rating, review)
        );
    }

    @Override
    public Flux<ProductReview> findProductReviewsForProduct(int productId) {
        return this.reactiveMongoTemplate
                .find(Query.query(Criteria.where("productId").is(productId)), ProductReview.class);
    }
}
