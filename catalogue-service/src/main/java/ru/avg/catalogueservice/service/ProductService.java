package ru.avg.catalogueservice.service;

import ru.avg.catalogueservice.entity.Product;

import java.util.Optional;

public interface ProductService {

    Iterable<Product> findAllProducts(String filter);

    Product createNewProduct(String title, String details);

    Optional<Product> findProductById(int productId);

    void updateProduct(Integer id, String title, String detail);

    void deleteProduct(Integer id);
}
