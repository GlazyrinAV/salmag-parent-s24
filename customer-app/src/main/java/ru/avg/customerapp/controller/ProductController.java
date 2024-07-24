package ru.avg.customerapp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.avg.customerapp.client.FavoriteProductsClient;
import ru.avg.customerapp.client.ProductsClient;
import ru.avg.customerapp.client.ProductsReviewClient;
import ru.avg.customerapp.controller.dto.NewProductReview;
import ru.avg.customerapp.entity.Product;
import ru.avg.customerapp.exception.ClientBadRequestException;

import java.util.NoSuchElementException;

@Controller
@RequiredArgsConstructor
@RequestMapping("customer/products/{productId:\\d+}")
public class ProductController {

    private final ProductsClient productsClient;

    private final FavoriteProductsClient favoriteProductsClient;

    private final ProductsReviewClient productsReviewClient;

    @ModelAttribute(name = "product", binding = false)
    public Mono<Product> loadProduct(@PathVariable("productId") int productId) {
        return this.productsClient.findProduct(productId)
                .switchIfEmpty(Mono.error(new NoSuchElementException("customer.errors.product.not_found")));
    }

    @GetMapping
    public Mono<String> getProductPage(Model model,
                                       @PathVariable("productId") int productId) {
        model.addAttribute("isFavorite", false);
        return this.productsReviewClient.findProductReviewsByProductId(productId)
                .collectList()
                .doOnNext(productReviews -> model.addAttribute("reviews", productReviews))
                .then(this.favoriteProductsClient.findFavoriteProduct(productId)
                        .doOnNext(favoriteProduct -> model.addAttribute("isFavorite", true)))
                .thenReturn("customer/products/product");
    }

    @PostMapping("add-to-favorites")
    public Mono<String> addToFavorites(@ModelAttribute(name = "product") Mono<Product> productMono) {
        return productMono
                .map(Product::id)
                .flatMap(productId -> this.favoriteProductsClient.addProductToFavorites(productId)
                        .thenReturn("redirect:/customer/products/%d".formatted(productId))
                        .onErrorResume(ex -> Mono.just("redirect:/customer/products/%d".formatted(productId))));
    }

    @PostMapping("remove-from-favorites")
    public Mono<String> deleteToFavorites(@ModelAttribute(name = "product") Mono<Product> productMono) {
        return productMono
                .map(Product::id)
                .flatMap(productId -> this.favoriteProductsClient.removeProductFromFavorites(productId)
                        .thenReturn("redirect:/customer/products/%d".formatted(productId)));
    }

    @PostMapping("create-review")
    public Mono<String> createReview(@PathVariable("productId") int productId,
                                     NewProductReview newProductReview,
                                     Model model) {
        return this.productsReviewClient.createProductReview(productId, newProductReview.rating(), newProductReview.review())
                .thenReturn("redirect:/customer/products/%d".formatted(productId))
                .onErrorResume(ClientBadRequestException.class, ex -> {
                    model.addAttribute("isFavorite", false);
                    model.addAttribute("dto", newProductReview);
                    model.addAttribute("errors", ex.getErrors());
                    return this.favoriteProductsClient.findFavoriteProduct(productId)
                            .doOnNext(favoriteProduct -> model.addAttribute("isFavorite", true))
                            .thenReturn("customer/products/product");
                });
    }

    @ExceptionHandler(NoSuchElementException.class)
    public String handleException(RuntimeException e, Model model) {
        model.addAttribute("error", e.getMessage());
        return "customer/errors/404";
    }
}
