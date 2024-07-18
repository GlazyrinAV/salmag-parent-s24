package ru.avg.managerapp.client;

import ru.avg.managerapp.entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRestClient {

    List<Product> findAllProducts(String filter);

    Product createNewProduct(String title, String details);

    Optional<Product> findProductById(int productId);

    void updateProduct(int productId, String title, String details);

    void deleteProduct(int productId);
}
