package com.jelleglebbeek.pcparts.product;

import com.jelleglebbeek.pcparts.exceptions.EntityNotFoundException;
import com.jelleglebbeek.pcparts.product.entities.Product;
import com.jelleglebbeek.pcparts.product.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Iterable<Product> findAll() {
        return this.productRepository.findAll();
    }

    public Iterable<Product> findByCategory(String category) {
        return this.productRepository.findAllByCategory_Name(category);
    }

    public Iterable<Product> findMultiple(List<UUID> ids) {
        return this.productRepository.findAllById(ids);
    }

    public Product findOne(UUID id) {
        return this.productRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(Product.class));
    }

    public Product create(Product product) {
        return this.productRepository.save(product);
    }

    public Product update(UUID id, Product product) {
        Product current = this.findOne(id);
        if (product.getName() != null) current.setName(product.getName());
        if (product.getDescription() != null) current.setDescription(product.getDescription());
        if (product.getCategory() != null) current.setCategory(product.getCategory());
        if (product.getPrice() != null) current.setPrice(product.getPrice());
        if (product.getHasStock() != null) current.setHasStock(product.getHasStock());
        if (product.getExternalUrl() != null) current.setExternalUrl(product.getExternalUrl());
        if (product.getProperties() != null) current.setProperties(product.getProperties());
        if (product.getConditions() != null) current.setConditions(product.getConditions());
        return this.productRepository.save(current);
    }

    public void delete(UUID id) {
        this.productRepository.deleteById(id);
    }
}
