package com.jelleglebbeek.pcparts.product.repositories;

import com.jelleglebbeek.pcparts.product.entities.Product;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface ProductRepository extends CrudRepository<Product, UUID> {
    Iterable<Product> findAllByCategory_Name(String categoryName);
}
