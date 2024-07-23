package ru.avg.customerapp.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.avg.customerapp.client.ProductsClient;
import ru.avg.customerapp.controller.dto.NewProductReview;
import ru.avg.customerapp.entity.Product;
import ru.avg.customerapp.service.FavoriteProductsService;
import ru.avg.customerapp.service.ProductReviewsService;

import java.util.NoSuchElementException;

@Controller
@RequiredArgsConstructor
@RequestMapping("customer/products/{productId:\\d+}")
public class ProductController {

    private final ProductsClient productsClient;

    private final FavoriteProductsService favoriteProductsService;

    private final ProductReviewsService productReviewsService;

    @ModelAttribute(name = "product", binding = false)
    public Mono<Product> loadProduct(@PathVariable("productId") int productId) {
        return this.productsClient.findProduct(productId)
                .switchIfEmpty(Mono.error(new NoSuchElementException("customer.errors.product.not_found")));
    }

    @GetMapping
    public Mono<String> getProductPage(Model model,
                                       @PathVariable("productId") int productId) {
        model.addAttribute("isFavorite", false);
        return this.productReviewsService.findProductReviewsForProduct(productId)
                .collectList()
                .doOnNext(productReviews -> model.addAttribute("reviews", productReviews))
                .then(this.favoriteProductsService.findFavoriteProduct(productId)
                        .doOnNext(favoriteProduct -> model.addAttribute("isFavorite", true)))
                .thenReturn("customer/products/product");
    }

    @PostMapping("add-to-favorites")
    public Mono<String> addToFavorites(@ModelAttribute(name = "product") Mono<Product> productMono) {
        return productMono
                .map(Product::id)
                .flatMap(productId -> this.favoriteProductsService.addProductToFavorites(productId)
                        .thenReturn("redirect:/customer/products/%d".formatted(productId)));
    }

    @PostMapping("remove-from-favorites")
    public Mono<String> deleteToFavorites(@ModelAttribute(name = "product") Mono<Product> productMono) {
        return productMono
                .map(Product::id)
                .flatMap(productId -> this.favoriteProductsService.removeProductFromFavorites(productId)
                        .thenReturn("redirect:/customer/products/%d".formatted(productId)));
    }

    @PostMapping("create-review")
    public Mono<String> createReview(@PathVariable("productId") int productId,
                                     @Valid NewProductReview newProductReview,
                                     BindingResult bindingResult,
                                     Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("dto", newProductReview);
            model.addAttribute("isFavorite", false);
            model.addAttribute("errors", bindingResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .toList());
            return this.favoriteProductsService.findFavoriteProduct(productId)
                    .doOnNext(favoriteProduct -> model.addAttribute("isFavorite", true))
                    .thenReturn("customer/products/product");
        } else {
            return this.productReviewsService.createReview(productId, newProductReview.rating(), newProductReview.review())
                    .thenReturn("redirect:/customer/products/%d".formatted(productId));
        }
    }

    @ExceptionHandler(NoSuchElementException.class)
    public String handleException(RuntimeException e, Model model) {
        model.addAttribute("error", e.getMessage());
        return "customer/errors/404";
    }
}
