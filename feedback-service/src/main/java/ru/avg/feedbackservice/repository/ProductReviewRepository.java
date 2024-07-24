package ru.avg.feedbackservice.repository;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import ru.avg.feedbackservice.entity.ProductReview;

import java.util.UUID;

public interface ProductReviewRepository extends ReactiveCrudRepository<ProductReview, UUID> {

    @Query("{'productId' : ?0}")
    Flux<ProductReview> findAllByProductId(int productId);
}
