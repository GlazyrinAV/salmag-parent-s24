package ru.avg.customerapp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Mono;
import ru.avg.customerapp.client.ProductsClient;

@Controller
@RequiredArgsConstructor
@RequestMapping("customer/products")
public class ProductsController {

    private final ProductsClient productsClient;

    @GetMapping("list")
    public Mono<String> getProductsListPage(@RequestParam(name = "filter", required = false) String filter,
                                            Model model) {
        return this.productsClient.findAllProducts(filter)
                .collectList()
                .doOnNext(products -> model.addAttribute("products", products))
                .thenReturn("customer/products/list");
    }
}
