package ru.avg.customerapp.client;

import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import ru.avg.customerapp.entity.Product;

@RequiredArgsConstructor
public class WebClientProductsClient implements ProductsClient {

    private final WebClient webClient;

    @Override
    public Flux<Product> findAllProducts(String filter) {
        return this.webClient.get()
                .uri("/catalogue-api/products?filter={filter}", filter)
                .retrieve()
                .bodyToFlux(Product.class);
    }
}
