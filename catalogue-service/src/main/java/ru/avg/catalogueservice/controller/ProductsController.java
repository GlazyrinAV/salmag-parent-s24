package ru.avg.catalogueservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.StringToClassMapItem;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import ru.avg.catalogueservice.controller.dto.NewProductDto;
import ru.avg.catalogueservice.entity.Product;
import ru.avg.catalogueservice.service.ProductService;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("catalogue-api/products")
public class ProductsController {

    private final ProductService productService;

    @GetMapping
    @Operation(security = @SecurityRequirement(name = "keycloak"))
    public Iterable<Product> findProducts(@RequestParam(value = "filter", required = false) String filter) {
        return this.productService.findAllProducts(filter);
    }

    @PostMapping
    @Operation(security = @SecurityRequirement(name = "keycloak"),
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = {
                            @Content(schema = @Schema(
                                    contentMediaType = MediaType.APPLICATION_JSON_VALUE,
                                    type = "object",
                                    properties = {
                                            @StringToClassMapItem(key = "title", value = String.class),
                                            @StringToClassMapItem(key = "details", value = String.class)
                                    }
                            ))
                    }
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            headers = @Header(name = "Content-Type", description = "тип данных"),
                            content = {
                                    @Content(schema = @Schema(
                                            contentMediaType = MediaType.APPLICATION_JSON_VALUE,
                                            type = "object",
                                            properties = {
                                                    @StringToClassMapItem(key = "id", value = Integer.class),
                                                    @StringToClassMapItem(key = "title", value = String.class),
                                                    @StringToClassMapItem(key = "details", value = String.class)
                                            }
                                    ))
                            }
                    )
            })
    public ResponseEntity<?> createProduct(@Valid @RequestBody NewProductDto dto,
                                           BindingResult bindingResult,
                                           UriComponentsBuilder uriBuilder)
            throws BindException {
        if (bindingResult.hasErrors()) {
            if (bindingResult instanceof BindException exception) {
                throw exception;
            } else {
                throw new BindException(bindingResult);
            }
        } else {
            Product product = this.productService.createNewProduct(dto.title(), dto.details());
            return ResponseEntity
                    .created(uriBuilder
                            .replacePath("/catalogue-api/products/{productId}")
                            .build(Map.of("productId", product.getId())))
                    .body(product);
        }
    }
}
