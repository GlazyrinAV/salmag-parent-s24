package ru.avg.catalogueservice.repository;

import org.springframework.data.repository.CrudRepository;
import ru.avg.catalogueservice.entity.Product;

public interface ProductRepository extends CrudRepository<Product, Integer> {
}
