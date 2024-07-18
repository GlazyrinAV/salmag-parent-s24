package ru.avg.managerapp.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.avg.managerapp.client.ProductRestClient;
import ru.avg.managerapp.controller.dto.UpdateProductDto;
import ru.avg.managerapp.entity.Product;
import ru.avg.managerapp.exception.BadRequestException;

import java.util.Locale;
import java.util.NoSuchElementException;

@Controller
@RequiredArgsConstructor
@RequestMapping("catalogue/products/{productId:\\d+}")
public class ProductController {

    private final ProductRestClient productRestClient;

    private final MessageSource messageSource;

    @ModelAttribute("product")
    public Product getProduct(@PathVariable int productId) {
        return this.productRestClient.findProductById(productId)
                .orElseThrow(() -> new NoSuchElementException("catalogue.errors.product.not_found"));
    }

    @GetMapping()
    public String getProduct() {
        return "catalogue/products/product";
    }

    @GetMapping("edit")
    public String getProductEditPage() {
        return "catalogue/products/edit";
    }

    @PostMapping("edit")
    public String updateProduct(@ModelAttribute(value = "product", binding = false) Product product,
                                UpdateProductDto updateProductDto, Model model) {
        try {
            this.productRestClient.updateProduct(product.id(), updateProductDto.title(), updateProductDto.details());
            return "redirect:/catalogue/products/%d".formatted(product.id());
        } catch (BadRequestException e) {
            model.addAttribute("dto", updateProductDto);
            model.addAttribute("errors", e.getErrors());
            return "catalogue/products/edit";
        }
    }

    @PostMapping("delete")
    public String deleteProduct(@ModelAttribute("product") Product product) {
        this.productRestClient.deleteProduct(product.id());
        return "redirect:/catalogue/products/list";
    }

    @ExceptionHandler(NoSuchElementException.class)
    public String handleNoSuchElementException(NoSuchElementException e, Model model, HttpServletResponse response,
                                               Locale locale) {
        model.addAttribute("error",
                this.messageSource.getMessage(e.getMessage(), new Object[0], e.getMessage(), locale));
        response.setStatus(HttpStatus.NOT_FOUND.value());
        return "errors/404";
    }
}
