package ru.avg.managerapp.service;

import ru.avg.managerapp.entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    List<Product> findAllProducts();

    Product createNewProduct(String title, String details);

    Optional<Product> findProductById(int productId);

    void updateProduct(Integer id, String title, String detail);

    void deleteProduct(Integer id);
}
