package ru.avg.managerapp.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
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
    public String createProduct(@Valid NewProductDto newProductDto,
                                BindingResult bindingResult,
                                Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("dto", newProductDto);
            model.addAttribute("errors", bindingResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .toList());
            return "catalogue/products/new_product";
        } else {
            Product product = this.productService.createNewProduct(newProductDto.title(), newProductDto.details());
            return "redirect:/catalogue/products/%d".formatted(product.getId());
        }
    }
}
