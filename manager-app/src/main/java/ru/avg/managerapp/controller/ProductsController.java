package ru.avg.managerapp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.avg.managerapp.controller.dto.NewProductDto;
import ru.avg.managerapp.entity.Product;
import ru.avg.managerapp.service.ProductService;

@Controller
@RequiredArgsConstructor
@RequestMapping("catalogue/products/")
public class ProductsController {

    private final ProductService productService;

    @GetMapping("list")
    public String getProductsList(Model model) {
        model.addAttribute("products", this.productService.findAllProducts());
        return "catalogue/products/list";
    }

    @GetMapping("create")
    public String getNewProductPage() {
        return "catalogue/products/new_product";
    }

    @PostMapping("create")
    public String createProduct(NewProductDto newProductDto) {
        Product product = this.productService.createNewProduct(newProductDto.title(), newProductDto.details());
        return "redirect:/catalogue/products/%d".formatted(product.getId());
    }
}
