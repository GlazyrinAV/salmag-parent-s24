package ru.avg.customerapp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Mono;
import ru.avg.customerapp.client.FavoriteProductsClient;
import ru.avg.customerapp.client.ProductsClient;
import ru.avg.customerapp.entity.FavoriteProduct;

@Controller
@RequiredArgsConstructor
@RequestMapping("customer/products/")
public class ProductsController {

    private final ProductsClient productsClient;

    private final FavoriteProductsClient favoriteProductsClient;

    @GetMapping("list")
    public Mono<String> getProductsListPage(@RequestParam(name = "filter", required = false) String filter,
                                            Model model) {
        model.addAttribute("filter", filter);
        return this.productsClient.findAllProducts(filter)
                .collectList()
                .doOnNext(products -> model.addAttribute("products", products))
                .thenReturn("customer/products/list");
    }

    @GetMapping("favorites")
    public Mono<String> getFavoriteProductsPage(@RequestParam(name = "filter", required = false) String filter,
                                                Model model) {
        model.addAttribute("filter", filter);
        return this.favoriteProductsClient.findAllFavoriteProducts()
                .map(FavoriteProduct::productId)
                .collectList()
                .flatMap(favoriteProducts -> this.productsClient.findAllProducts(filter)
                        .filter(product -> favoriteProducts.contains(product.id()))
                        .collectList()
                        .doOnNext(products -> model.addAttribute("products", products)))
                .thenReturn("customer/products/favorites");

    }
}
