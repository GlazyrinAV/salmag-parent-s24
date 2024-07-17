package ru.avg.managerapp.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import ru.avg.managerapp.controller.dto.UpdateProductDto;
import ru.avg.managerapp.entity.Product;
import ru.avg.managerapp.service.ProductService;

import java.util.Locale;
import java.util.NoSuchElementException;

@Controller
@RequiredArgsConstructor
@RequestMapping("catalogue/products/{productId:\\d+}")
public class ProductController {

    private final ProductService productService;

    private final MessageSource messageSource;

    @ModelAttribute("product")
    public Product getProduct(@PathVariable int productId) {
        return this.productService.findProductById(productId)
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
    public String updateProduct(@ModelAttribute(value = "product", binding = false) Product product, @Valid UpdateProductDto updateProductDto,
                                BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("dto", updateProductDto);
            model.addAttribute("errors", bindingResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .toList());
            return "catalogue/products/edit";
        } else {
            this.productService.updateProduct(product.getId(), updateProductDto.title(), updateProductDto.details());
            return "redirect:/catalogue/products/%d".formatted(product.getId());
        }
    }

    @PostMapping("delete")
    public String deleteProduct(@ModelAttribute("product") Product product) {
        this.productService.deleteProduct(product.getId());
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
