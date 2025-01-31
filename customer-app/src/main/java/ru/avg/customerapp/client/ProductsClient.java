package ru.avg.customerapp.client;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.avg.customerapp.entity.Product;

public interface ProductsClient {

    Flux<Product> findAllProducts(String filter);

    Mono<Product> findProduct(int productId);
}
