package ru.avg.managerapp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.avg.managerapp.client.ProductRestClient;
import ru.avg.managerapp.controller.dto.NewProductDto;
import ru.avg.managerapp.entity.Product;
import ru.avg.managerapp.exception.BadRequestException;

@Controller
@RequiredArgsConstructor
@RequestMapping("catalogue/products/")
public class ProductsController {

    private final ProductRestClient productRestClient;

    @GetMapping("list")
    public String getProductsList(Model model) {
        model.addAttribute("products", this.productRestClient.findAllProducts());
        return "catalogue/products/list";
    }

    @GetMapping("create")
    public String getNewProductPage() {
        return "catalogue/products/new_product";
    }

    @PostMapping("create")
    public String createProduct(NewProductDto newProductDto,
                                Model model) {
        try {
            Product product = this.productRestClient.createNewProduct(newProductDto.title(), newProductDto.details());
            return "redirect:/catalogue/products/%d".formatted(product.id());
        } catch (BadRequestException e) {
            model.addAttribute("dto", newProductDto);
            model.addAttribute("errors", e.getErrors());
            return "catalogue/products/new_product";
        }
    }
}

