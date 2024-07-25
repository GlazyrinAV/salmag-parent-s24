package ru.avg.feedbackservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.avg.feedbackservice.entity.FavoriteProduct;
import ru.avg.feedbackservice.repository.FavoriteProductRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DefaultFavoriteProductsService implements FavoriteProductsService {

    private final FavoriteProductRepository favoriteProductRepository;

    @Override
    public Mono<FavoriteProduct> addProductToFavorites(int productId, String userId) {
        return this.favoriteProductRepository.save(new FavoriteProduct(UUID.randomUUID(), productId, userId));
    }

    @Override
    public Mono<Void> removeProductFromFavorites(int productId, String userId) {
        return this.favoriteProductRepository.deleteByProductIdAndUserId(productId, userId);
    }

    @Override
    public Mono<FavoriteProduct> findFavoriteProduct(int productId, String userId) {
        return this.favoriteProductRepository.findByProductIdAndUserId(productId, userId);
    }

    @Override
    public Flux<FavoriteProduct> findAllFavoriteProducts(String userId) {
        return this.favoriteProductRepository.findAllByUserId(userId);
    }
}
