package ru.avg.feedbackservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.avg.feedbackservice.controller.dto.NewProductReview;
import ru.avg.feedbackservice.entity.ProductReview;
import ru.avg.feedbackservice.service.ProductReviewsService;

@RestController
@RequiredArgsConstructor
@RequestMapping("feedback-api/product-reviews")
public class ProductReviewController {

    private final ProductReviewsService productReviewsService;

    @GetMapping("by-product-id/{productId:\\d+}")
    public Flux<ProductReview> findProductReviewsByProductId(@PathVariable("productId") int productId) {
        return this.productReviewsService.findProductReviewsForProduct(productId);
    }

    @PostMapping
    public Mono<ResponseEntity<ProductReview>> saveProductReview(@Valid @RequestBody Mono<NewProductReview> dto,
                                                                 UriComponentsBuilder uriBuilder) {
        return dto
                .flatMap(newProductReview -> this.productReviewsService.createReview(newProductReview.productId()
                        , newProductReview.rating(), newProductReview.review()))
                .map(productReview -> ResponseEntity
                        .created(uriBuilder
                                .replacePath("/feedback-api/product-reviews/{id}")
                                .build(productReview.getId()))
                        .body(productReview));
    }
}
