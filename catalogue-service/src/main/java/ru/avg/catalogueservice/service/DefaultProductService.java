package ru.avg.catalogueservice.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.avg.catalogueservice.entity.Product;
import ru.avg.catalogueservice.repository.ProductRepository;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DefaultProductService implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public Iterable<Product> findAllProducts(String filter) {
        if (filter == null || filter.isBlank()) {
            return this.productRepository.findAll();
        } else {
            return this.productRepository.findAllByTitleLikeIgnoreCase("%" + filter + "%");
        }
    }

    @Override
    @Transactional
    public Product createNewProduct(String title, String details) {
        return this.productRepository.save(new Product(null, title, details));
    }

    @Override
    public Optional<Product> findProductById(int productId) {
        return this.productRepository.findById(productId);
    }

    @Override
    @Transactional
    public void updateProduct(Integer id, String title, String detail) {
        this.productRepository.findById(id).ifPresentOrElse(product -> {
            product.setTitle(title);
            product.setDetails(detail);
        }, NoSuchElementException::new);
    }

    @Override
    @Transactional
    public void deleteProduct(Integer id) {
        this.productRepository.deleteById(id);
    }


}
