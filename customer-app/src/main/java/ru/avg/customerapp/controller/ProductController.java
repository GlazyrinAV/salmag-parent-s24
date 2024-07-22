package ru.avg.customerapp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.avg.customerapp.client.ProductsClient;
import ru.avg.customerapp.entity.Product;
import ru.avg.customerapp.service.FavoriteProductsService;

@Controller
@RequiredArgsConstructor
@RequestMapping("customer/products/{productId:\\d+}")
public class ProductController {

    private final ProductsClient productsClient;

    private final FavoriteProductsService favoriteProductsService;

    @ModelAttribute(name = "product", binding = false)
    public Mono<Product> loadProduct(@PathVariable("productId") int productId) {
        return this.productsClient.findProduct(productId);
    }

    @GetMapping
    public String getProductPage() {
        return "customer/products/product";
    }

    @PostMapping("add-to-favorites")
    public Mono<String> addToFavorites(@ModelAttribute(name = "product") Mono<Product> productMono) {
        return productMono
                .map(Product::id)
                .flatMap(productId -> this.favoriteProductsService.addProductToFavorites(productId)
                        .thenReturn("redirect:/customer/products/%d".formatted(productId)));
    }

    @DeleteMapping
    public Mono<String> deleteToFavorites(@ModelAttribute(name = "product") Mono<Product> productMono) {
        return productMono
                .map(Product::id)
                .flatMap(productId -> this.favoriteProductsService.removeProductFromFavorites(productId)
                        .thenReturn("redirect:/customer/products/%d".formatted(productId)));
    }

}
