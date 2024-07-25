package ru.avg.feedbackservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
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
    public Flux<FavoriteProduct> findFavoriteProducts(Mono<JwtAuthenticationToken> tokenMono) {
        return tokenMono.flatMapMany(token -> this.favoriteProductsService
                .findAllFavoriteProducts(token.getToken().getSubject()));
    }

    @GetMapping("by-product-id/{productId:\\d+}")
    public Mono<FavoriteProduct> findFavoriteProductById(@PathVariable("productId") int productId,
                                                         Mono<JwtAuthenticationToken> tokenMono) {
        return tokenMono.flatMap(token -> this.favoriteProductsService
                .findFavoriteProduct(productId, token.getToken().getSubject()))
                ;
    }

    @PostMapping()
    public Mono<ResponseEntity<FavoriteProduct>> addFavoriteProduct(
            @Valid @RequestBody Mono<NewFavoriteProduct> newFavoriteProduct,
            UriComponentsBuilder uriBuilder,
            Mono<JwtAuthenticationToken> tokenMono) {
        return Mono.zip(newFavoriteProduct, tokenMono)
                .flatMap(tuple -> this.favoriteProductsService
                        .addProductToFavorites(tuple.getT1().productId(), tuple.getT2().getToken().getSubject())
                        .map(product -> ResponseEntity.created(uriBuilder
                                        .replacePath("feedback-api/favorite-products/{id}")
                                        .build(product.getProductId()))
                                .body(product))
                );
    }

    @DeleteMapping("by-product-id/{productId:\\d+}")
    public Mono<ResponseEntity<Void>> removeProductFromFavorites(@PathVariable("productId") int productId,
                                                                 Mono<JwtAuthenticationToken> tokenMono) {
        return tokenMono.flatMap(token -> this.favoriteProductsService
                .removeProductFromFavorites(productId, token.getToken().getSubject())
                .then(Mono.just(ResponseEntity.noContent().build())));
    }
}
