package com.jelleglebbeek.pcparts.product;

import com.jelleglebbeek.pcparts.exceptions.EntityNotFoundException;
import com.jelleglebbeek.pcparts.product.entities.Category;
import com.jelleglebbeek.pcparts.product.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Iterable<Category> findAll() {
        return this.categoryRepository.findAll();
    }

    public Category findOne(String id) {
        return this.categoryRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(Category.class));
    }

    public Category create(Category category) {
        return this.categoryRepository.save(category);
    }

    public Category update(String id, Category category) {
        Category current = this.findOne(id);
        if (category.getName() != null) current.setName(category.getName());
        if (category.getDescription() != null) current.setDescription(category.getDescription());
        return this.categoryRepository.save(current);
    }

    public void delete(String id) {
        this.categoryRepository.deleteById(id);
    }

}
