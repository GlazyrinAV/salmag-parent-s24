package ru.avg.feedbackservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.avg.feedbackservice.controller.dto.NewFavoriteProduct;
import ru.avg.feedbackservice.entity.FavoriteProduct;
import ru.avg.feedbackservice.service.FavoriteProductsService;

@RestController
@RequiredArgsConstructor
@RequestMapping("feedback-api/favorite-products")
public class FavoriteProductController {

    private final FavoriteProductsService favoriteProductsService;

    @GetMapping
    public Flux<FavoriteProduct> findFavoriteProducts() {
        return this.favoriteProductsService.findAllFavoriteProducts();
    }

    @GetMapping("by-product-id/{productId:\\d+}")
    public Mono<FavoriteProduct> findFavoriteProductById(@PathVariable("productId") int productId) {
        return favoriteProductsService.findFavoriteProduct(productId);
    }

    @PostMapping()
    public Mono<ResponseEntity<FavoriteProduct>> addFavoriteProduct(
            @Valid @RequestBody Mono<NewFavoriteProduct> newFavoriteProduct,
            UriComponentsBuilder uriBuilder) {
        return newFavoriteProduct.flatMap(dto -> this.favoriteProductsService.addProductToFavorites(dto.productId())
                .map(product -> ResponseEntity.created(uriBuilder
                                .replacePath("feedback-api/favorite-products/{id}")
                                .build(product.getProductId()))
                        .body(product))
        );
    }

    @DeleteMapping("by-product-id/{productId:\\d+}")
    public Mono<ResponseEntity<Void>> removeProductFromFavorites(@PathVariable("productId") int productId) {
        return this.favoriteProductsService.removeProductFromFavorites(productId)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}
